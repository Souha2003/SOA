import { Injectable, HttpException, HttpStatus } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { HttpService } from '@nestjs/axios'; // Import pour la communication
import { firstValueFrom } from 'rxjs'; // Pour gérer les appels API
import PDFDocument from 'pdfkit';
import * as fs from 'fs';
import { Document } from './document.entity';

@Injectable()
export class DocumentService {
  constructor(
    @InjectRepository(Document)
    private docRepo: Repository<Document>,
    private readonly httpService: HttpService, // Injection du client HTTP
  ) {}

  async generateConvention(studentId: number) {
    let studentData;

    // 1. Appels au microservice Spring Boot pour récupérer l'étudiant
    try {
      const springUrl = `http://localhost:8080/api/students/${studentId}`;
      const response = await firstValueFrom(this.httpService.get(springUrl));
      studentData = response.data;
    } catch (error) {
      throw new HttpException(
        "Impossible de générer le document : Étudiant introuvable sur le service Spring Boot.",
        HttpStatus.NOT_FOUND,
      );
    }

    // 2. Préparation du fichier PDF
    const fileName = `convention-${studentId}-${Date.now()}.pdf`;
    const folderPath = './uploads';
    const filePath = `${folderPath}/${fileName}`;

    if (!fs.existsSync(folderPath)) {
      fs.mkdirSync(folderPath, { recursive: true });
    }

    // 3. Génération du contenu PDF avec les vraies données
    const doc = new PDFDocument();
    doc.pipe(fs.createWriteStream(filePath));

    doc.fontSize(22).text('CONVENTION DE STAGE', { align: 'center', underline: true });
    doc.moveDown();
    
    doc.fontSize(14).text(`Nom de l'étudiant : ${studentData.studentName}`);
    doc.text(`Email : ${studentData.email}`);
    doc.text(`Université : ${studentData.university}`);
    doc.text(`Entreprise d'accueil : ${studentData.companyName}`);
    doc.moveDown();
    
    doc.fontSize(12).text(`Statut de la demande : ${studentData.status}`, { oblique: true });
    doc.text(`Date de génération : ${new Date().toLocaleDateString()}`);

    doc.end();

    // 4. Sauvegarde des métadonnées dans la base de données NestJS
    const document = this.docRepo.create({
      studentId,
      fileName,
      filePath,
      type: 'CONVENTION',
    });

    return this.docRepo.save(document);
  }

  findAllByStudent(studentId: number) {
    return this.docRepo.find({ where: { studentId } });
  }

  findOne(id: number) {
    return this.docRepo.findOneBy({ id });
  }

  async delete(id: number) {
    const doc = await this.docRepo.findOneBy({ id });

    if (doc?.filePath && fs.existsSync(doc.filePath)) {
      fs.unlinkSync(doc.filePath);
    }

    return this.docRepo.delete(id);
  }
}