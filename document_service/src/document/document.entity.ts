import { Entity, PrimaryGeneratedColumn, Column } from 'typeorm';

@Entity()
export class Document {
  @PrimaryGeneratedColumn()
  id!: number;  // ✅ Le "!" dit à TypeScript "ça va être initialisé plus tard"

  @Column({ type: 'int' })
  studentId!: number;

  @Column()
  fileName!: string;

  @Column()
  filePath!: string;

  @Column({ default: 'CONVENTION' })
  type!: string;
}