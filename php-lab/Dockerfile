FROM php:8.3-apache

WORKDIR /var/www/html

COPY index.php ./

RUN mkdir -p uploads \
    && chown -R www-data:www-data /var/www/html/uploads

EXPOSE 80

