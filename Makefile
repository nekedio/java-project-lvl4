path = $(shell pwd)

run:
	APP_ENV=development gradle run

lint:
	./gradlew checkstyleMain
	./gradlew checkstyleTest

check-updates:
	./gradlew dependencyUpdates

migrate:
	gradle generateMigrations

clean:
	./gradlew clean

test:
	./gradlew test

report: test
	./gradlew jacocoTestReport
	@echo "\n\nOpen the following file in any browser:"
	@echo "\033[34mfile://${path}/build/jacocoHtml/index.html\033[0m"
	@echo "---------------------------------------------------------------------------------------------------"
	@w3m -dump file://${path}/build/jacocoHtml/index.html
	@echo "---------------------------------------------------------------------------------------------------"