# xakaton-bimit-edge

## Подготовка проекта

Установить postgres
 - Завести пользователя: xakaton
 - Завести базу данных: xakaton
 
Установить брокер (например MQTT брокер Mosquitto)


## Запуск проекта из docker'а

Минимальный запуск
```
docker run -e DATABASE=//IP_ADRESS:5432/xakaton  docker.pkg.github.com/maks-master/xakaton-bimit-web/docker-xakaton-web:work
```

Все параметры
```
docker run --name docker-xakaton-web -e DATABASE=//IP_ADRESS:5432/xakaton --rm -d -v ./logs:/usr/local/tomcat/logs  docker.pkg.github.com/maks-master/xakaton-bimit-web/docker-xakaton-web:work
```
