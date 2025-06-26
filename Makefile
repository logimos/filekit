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

version:
	@git describe --tags --abbrev=0
	
patch: 
	@$(MAKE) bump VERSION_TYPE=patch

minor: 
	@$(MAKE) bump VERSION_TYPE=minor

major: 
	@$(MAKE) bump VERSION_TYPE=major

bump:
	@bash -c '\
	set -ex; \
	if [ -z "$(shell git tag)" ]; then \
		git tag v0.0.0; \
	fi; \
	latest_tag=$$(git describe --tags --abbrev=0); \
	echo "Latest tag: $$latest_tag"; \
	IFS='.' read -ra parts <<< "$${latest_tag#v}"; \
	case "$(VERSION_TYPE)" in \
		patch) parts[2]=$$(($${parts[2]} + 1)) ;; \
		minor) parts[1]=$$(($${parts[1]} + 1)); parts[2]=0 ;; \
		major) parts[0]=$$(($${parts[0]} + 1)); parts[1]=0; parts[2]=0 ;; \
	esac; \
	new_tag="v$${parts[0]}.$${parts[1]}.$${parts[2]}"; \
	if git rev-parse $$new_tag >/dev/null 2>&1; then \
		echo "❌ Tag $$new_tag already exists!"; \
	else \
		echo "✅ Creating and pushing tag $$new_tag"; \
		git tag $$new_tag; \
		git push --tags; \
	fi'


