import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import axios from 'axios';
const Eureka = require('eureka-js-client').Eureka;

async function bootstrap() {
  // --- 1. RÉCUPÉRATION DE LA CONFIG DEPUIS SPRING CONFIG SERVER ---
  let remoteConfig = {};
  const configServerUrl = 'http://localhost:8888/document-service/default';

  try {
    const response = await axios.get(configServerUrl);
    // On extrait les propriétés du JSON envoyé par Spring
    response.data.propertySources.forEach((source: any) => {
      Object.assign(remoteConfig, source.source);
    });
    console.log('✅ Configuration récupérée avec succès depuis le Config Server');
  } catch (error) {
    console.error('⚠️ Config Server injoignable, utilisation des valeurs par défaut.');
  }

  // --- 2. DÉFINITION DES VARIABLES (Priorité à la config distante) ---
  const PORT = remoteConfig['server.port'] || process.env.PORT || 3000;
  const SERVICE_NAME = 'DOCUMENT-SERVICE';

  // --- 3. DÉMARRAGE DE L'APPLICATION NESTJS ---
  const app = await NestFactory.create(AppModule);
  await app.listen(PORT);
  console.log(`🚀 NestJS est prêt sur le port : ${PORT}`);

  // --- 4. ENREGISTREMENT SUR EUREKA ---
  const eurekaClient = new Eureka({
    instance: {
      app: SERVICE_NAME,
      hostName: 'localhost',
      ipAddr: '127.0.0.1',
      statusPageUrl: `http://localhost:${PORT}/info`,
      port: {
        '$': Number(PORT),
        '@enabled': true,
      },
      vipAddress: SERVICE_NAME,
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: 'localhost',
      port: 8761,
      servicePath: '/eureka/apps/',
    },
  });

  eurekaClient.start((error: any) => {
    if (error) {
      console.error('❌ Échec de l’enregistrement sur Eureka :', error);
    } else {
      console.log(`✨ ${SERVICE_NAME} est enregistré sur Eureka !`);
    }
  });
}
bootstrap();