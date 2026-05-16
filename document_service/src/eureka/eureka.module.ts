import { Module, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { EurekaService } from './eureka.service';

@Module({
  imports: [ConfigModule],
  providers: [EurekaService],
  exports: [EurekaService],
})
export class EurekaModule implements OnModuleInit, OnModuleDestroy {
  constructor(private readonly eurekaService: EurekaService) {}

  async onModuleInit() {
    // S'exécute quand le microservice démarre
    await this.eurekaService.register();
  }

  async onModuleDestroy() {
    // S'exécute proprement quand on coupe le serveur (Ctrl+C)
    await this.eurekaService.unregister();
  }
}