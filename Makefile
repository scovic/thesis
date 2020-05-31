build-jars-iam:
	cd ./iam	mvn clean && mvn package -Dmaven.test.failure.ignore=true

build-jars-ticket-seller:
	cd ./ticket-seller && mvn clean && mvn package -Dmaven.test.failure.ignore=true

build-jars-orchestration-service:
	cd ./orchestration-service && mvn clean && mvn package -Dmaven.test.failure.ignore=true

remove-docker-images:
	docker image rm orchestration-seller-service:latest ticket-seller-service:latest iam-service:latest

build-docker-images:	
	docker build --tag iam-service ./iam
	docker build --tag orchestration-seller-service ./orchestration-service
	docker build --tag ticket-seller-service ./ticket-seller

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