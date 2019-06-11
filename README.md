##Info:
Authentication and Authorization server using JWT, Spring Security.
#Build:

./gradlew clean build -x test

# Deploy:

java -jar ./build/libs/*.jar

## Generate token: 
```curl -x POST localhost:8012/auth/login?userName=user@gmail.com&password=password```

