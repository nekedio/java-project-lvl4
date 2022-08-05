run:
	gradle run

lint:
	./gradlew checkstyleMain

check-updates:
	./gradlew dependencyUpdates

migrate:
	gradle generateMigrations