import { Injectable, Logger } from '@nestjs/common';
import { Eureka } from 'eureka-js-client';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class EurekaService {
  private readonly logger = new Logger(EurekaService.name);
  private client: Eureka;
  private isRegistered = false;

  constructor(private configService: ConfigService) {
    this.initClient();
  }

  private initClient() {
    const serviceName = this.configService.get<string>('SERVICE_NAME', 'DOCUMENT-SERVICE').toUpperCase();
    const servicePort = parseInt(this.configService.get('PORT', '3000'), 10);
    const serviceHost = this.configService.get<string>('SERVICE_HOST', 'localhost');
    const eurekaHost = this.configService.get<string>('EUREKA_HOST', 'localhost');
    const eurekaPort = parseInt(this.configService.get('EUREKA_PORT', '8761'), 10);

    this.logger.log(`Initialisation Eureka : ${serviceName} -> http://${eurekaHost}:${eurekaPort}`);

    this.client = new Eureka({
      instance: {
        app: serviceName,
        // --- MODIFICATIONS ICI ---
        instanceId: `${serviceHost}:${serviceName}:${servicePort}`, 
        status: 'UP', 
        // -------------------------
        hostName: serviceHost,
        ipAddr: serviceHost,
        port: {
          '$': servicePort,
          '@enabled': true,
        },
        vipAddress: serviceName,
        dataCenterInfo: {
          '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
          name: 'MyOwn',
        },
        statusPageUrl: `http://${serviceHost}:${servicePort}/health`,
        healthCheckUrl: `http://${serviceHost}:${servicePort}/health`,
        homePageUrl: `http://${serviceHost}:${servicePort}`,
      },
      eureka: {
        host: eurekaHost,
        port: eurekaPort,
        servicePath: '/eureka/apps/',
        registerWithEureka: true,
        fetchRegistry: true,
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
        },
        heartbeatInterval: 30000,
        registryFetchInterval: 30000,
      },
    });

    this.client.on('started', () => this.logger.log('✔ Client Eureka démarré'));
    this.client.on('registered', () => {
      this.isRegistered = true;
      this.logger.log('✅ Service enregistré dans le registre Eureka');
    });
    this.client.on('deregistered', () => this.logger.warn('❌ Service retiré d\'Eureka'));
    this.client.on('error', (err) => this.logger.error(`🔥 Erreur Eureka: ${err.message}`));
  }

  async register() {
    this.client.start();
  }

  async unregister() {
    if (this.isRegistered) {
      this.client.stop();
    }
  }

  getServiceUrl(targetServiceName: string): string | null {
    try {
      const instances = this.client.getInstancesByAppId(targetServiceName.toUpperCase());
      if (instances && instances.length > 0) {
        return `http://${instances[0].hostName}:${instances[0].port.$}`;
      }
      return null;
    } catch (e) {
      this.logger.error(`Erreur recherche service ${targetServiceName}: ${e.message}`);
      return null;
    }
  }
}