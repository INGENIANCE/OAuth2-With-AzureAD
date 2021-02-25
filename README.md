# OAuth2-With-AzureAD

## Prérequis
> 📃 Cette API est utilisée dans le cadre d'un article disponible depuis le blog technique d'Ingéniance :
> https://blog.impulsebyingeniance.io/developper-et-deployer-une-application-web-vue-js-java-sous-docker-avec-une-authentification-sous-azure

Une configuration d'un compte *Azure Active Directory* est également nécessaire pour profiter pleinement de ce projet. Merci de consulter les articles suivants pour initialiser les applications sur un compte locataire Azure AD :
* https://blog.impulsebyingeniance.io/configurer-role-aad-pour-acces-applications
* https://blog.impulsebyingeniance.io/utiliser-les-roles-dazure-active-directory-avec-une-web-api-sous-spring-boot-pour-authentifier-les-utilisateurs

## Description

Ce projet vous permet de déployer localement une infrastructure orientée Web sécurisée à l'aide d'**Azure Active Directory**.

Les différents composants sont dockerisés et un *reverse-proxy* **Nginx** sert a distribuer les requêtes vers le sous-réseau Docker.

Les API sont développées en *Java* via le Framework **Spring Boot** et s'appuient sur le composant `com.azure.spring` du [SDK Java](https://github.com/Azure/azure-sdk-for-java) d'Azure pour gérer le protcole d'accès **OAuth 2.0**.

Le client Web quant à lui est développé en [vue.js](https://vuejs.org/) et les appels REST sont gérés par [Axios](https://github.com/axios/axios).

## Installation

Copiez le projet localement :
```shell
git clone https://github.com/INGENIANCE/OAuth2-With-AzureAD.git
```
Pensez à remplacer par vos identifiants Azure les éléments entre <> dans les fichiers d'environnement `application.yml` de chacune des API :

*Web App*
```yaml
azure:
  activedirectory:
    client-id: <Replace-with-your-WebApp-Client-ID>
    client-secret: <Replace-with-your-WebApi-URI-application-ID>
    tenant-id: <Replace-with-your-tenant-ID>
    authorization-clients:
      graph:
        scopes:
          - User.Read
          - Directory.Read.All
      ingeniance:
        scopes:
          - <Replace-with-your-WebApi-URI-application-ID>/User.Read
    post-logout-redirect-uri: http://localhost
```
*Web API*
```yaml
azure:
  activedirectory:
    client-id: <Replace-with-your-WebApp-Client-ID>
    app-id-uri: <Replace-with-your-WebApi-URI-application-ID>
```
A la racine du projet lancez la commande suivante :
```shell
docker-compose up -d
```