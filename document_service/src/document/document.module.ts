import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { HttpModule } from '@nestjs/axios'; // 1. Importez ceci en haut
import { DocumentService } from './document.service';
import { DocumentController } from './document.controller';
import { Document } from './document.entity';

@Module({
  imports: [
    TypeOrmModule.forFeature([Document]),
    HttpModule, // 2. Ajoutez-le ici dans la liste des imports
  ],
  controllers: [DocumentController],
  providers: [DocumentService],
})
export class DocumentModule {}