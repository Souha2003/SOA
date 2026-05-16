import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { DocumentModule } from './document/document.module';
import { EurekaModule } from './eureka/eureka.module';

@Module({
  imports: [
     EurekaModule,
    TypeOrmModule.forRoot({
      type: 'sqlite',           // Changement ici
      database: 'stage_db.sqlite',  // Un simple fichier
      autoLoadEntities: true,
      synchronize: true,
      // AJOUTEZ CETTE LIGN
    }),
    DocumentModule,
  ],
})
export class AppModule {}