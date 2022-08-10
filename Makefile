run:
	APP_ENV=development gradle run

lint:
	./gradlew checkstyleMain

check-updates:
	./gradlew dependencyUpdates

migrate:
	gradle generateMigrations

clean:
	./gradlew clean