docker cp joe-nginx:/etc/nginx/conf.d/default.conf D:\DIFFERENT\rboidos\DOCKER
docker cp joe-nginx:/etc/nginx/nginx.conf D:/DIFFERENT/rboidos/DOCKER
docker cp joe-nginx:/etc/nginx/sites-available/load_balancer.conf D:/DIFFERENT/rboidos/DOCKER

docker cp joe-activemq:/opt/activemq/conf/activemq.xml D:/DIFFERENT/rboidos/DOCKER
docker cp joe-activemq:/opt/activemq/conf/jetty.xml D:/DIFFERENT/rboidos/DOCKER

docker cp example_joe_postgres_1://var/lib/postgresql/data/postgresql.conf D:/DIFFERENT/rboidos/DOCKER