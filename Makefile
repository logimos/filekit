PROJECT = filekit

.PHONY: all test build publish clean

all: build

build:
	./gradlew build

test:
	./gradlew test

publish:
	./gradlew publish

clean:
	./gradlew clean
