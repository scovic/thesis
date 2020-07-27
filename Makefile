# JARS

build-jars-iam:
	cd ./iam &&	mvn clean && mvn package -Dmaven.test.failure.ignore=true

build-jars-ticket-seller:
	cd ./ticket-seller && mvn clean && mvn package -Dmaven.test.failure.ignore=true

build-jars-orchestration-service:
	cd ./orchestration-service && mvn clean && mvn package -Dmaven.test.failure.ignore=true

build-jars-post-service:
	cd ./postservice && mvn clean && mvn package -Dmaven.test.failure.ignore=true

build-jars-notification-service:
	cd ./notificationsservice && mvn clean && mvn package -Dmaven.test.failure.ignore=true


# DOCKER

remove-docker-images:
	docker image rm post-service:latest orchestration-service:latest ticket-seller-service:latest iam-service:latest notification-service:latest

build-docker-images:	
	docker build --tag iam-service ./iam
	docker build --tag orchestration-service ./orchestration-service
	docker build --tag ticket-seller-service ./ticket-seller
	docker build --tag post-service ./postservice
	docker build --tag notification-service ./notificationsservice


# START/STOP

base_start:
	docker-compose -f docker-compose-base.yml down --remove-orphans
	docker-compose -f docker-compose-base.yml up -d

base_stop:
	docker-compose -f docker-compose-base.yml down --remove-orphans

compose_start:
	docker-compose down
	docker-compose up

compose_stop:
	docker-compose down