# Banking Application

## 📝 Opis projektu
Aplikacja bankowa umożliwiająca zarządzanie kontami i przeprowadzanie operacji finansowych z wykorzystaniem aktualnych kursów walut z API NBP (Narodowego Banku Polskiego).

## 🛠️ Technologie
- Java
- Spring Boot
- Spring Web
- Spring Data
- Maven
- API NBP

## 🔄 Endpointy
*(Na podstawie obsługiwanych wyjątków można wywnioskować następujące endpointy)*

### Operacje na koncie
- Zarządzanie saldami kont
- Operacje wymiany walut
- Pobieranie kursów walut z NBP API

## ⚠️ Obsługa błędów
### Własne wyjątki biznesowe
1. `InsufficientBalanceException`
   - Kod błędu: `INSUFFICIENT_BALANCE`
   - Występuje gdy: Na koncie brak wystarczających środków

2. `AccountNotFoundException`
   - Kod błędu: `ACCOUNT_NOT_FOUND`
   - Występuje gdy: Konto o podanym identyfikatorze nie istnieje

3. `InvalidCurrentPairException`
   - Kod błędu: `INVALID_CURRENT_PAIR`
   - Występuje gdy: Nieprawidłowa para walutowa

### Błędy integracji z API NBP
Aplikacja obsługuje różne scenariusze błędów podczas komunikacji z API NBP:

1. **Brak danych** (HTTP 404)
   - Występuje gdy: Brak danych dla określonego zakresu czasowego
   - Kod błędu: `NOT_FOUND`

2. **Nieprawidłowe zapytanie** (HTTP 400)
   - Występuje gdy: Nieprawidłowo sformułowane zapytanie lub przekroczony limit danych
   - Kod błędu: `BAD_REQUEST`

3. **Błąd serwera** (HTTP 500)
   - Występuje gdy: Problemy z API NBP
   - Kod błędu: `API_ERROR`

## 🔍 Format odpowiedzi błędów
Wszystkie błędy zwracane są w ujednoliconym formacie JSON:
