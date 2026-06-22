FROM mono:6.12

RUN sed -i \
        -e 's|http://deb.debian.org/debian-security|http://archive.debian.org/debian-security|g' \
        -e 's|http://deb.debian.org/debian|http://archive.debian.org/debian|g' \
        -e '/buster-updates/d' \
        /etc/apt/sources.list \
    && apt-get -o Acquire::Check-Valid-Until=false update \
    && apt-get install -y --no-install-recommends mono-xsp4 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY Default.aspx Web.config ./

RUN mkdir -p uploads \
    && chmod -R 777 /app/uploads

EXPOSE 8080

CMD ["xsp4", "--address=0.0.0.0", "--port=8080", "--root=/app", "--nonstop", "--verbose", "--printlog"]
