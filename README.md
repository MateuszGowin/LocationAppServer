
# LocationApp Server

Location app REST API


## Technologies
- Java
- Spring Boot
- Gradle
- Spring Data JPA
- Spring Security
- MySQL
- JWT (JSON Web Token)


## Some API Reference

#### Authenticate user
```http
  POST /api/auth/signin
```
#### Reset password

```http
  POST /api/auth/resetPassword
```

#### Get list of all places
```http
  GET /place/all
```
#### Get list of all places in radius
```http
  GET /place/allNearbyPlaces/{latitude}/{longitude}/{radius}
```
| Parameter | Type     |
| :-------- | :------- |
| `latitude` | `BigDecimal` |
| `longitude` | `BigDecimal` |
| `radius` | `int` |

#### Get list opinions for place
```http
  GET /place/{placeId}/opinions
```
| Parameter | Type     |
| :-------- | :------- |
| `placeId` | `Long` |

#### Add new opinion
```http
  POST /opinion
```

