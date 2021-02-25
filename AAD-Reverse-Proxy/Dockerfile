FROM nginx:stable-alpine
LABEL maintainer="Cyrille P."
COPY build.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]
