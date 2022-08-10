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

start-dist:
	APP_ENV=production ./build/install/app/bin/app