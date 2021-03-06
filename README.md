# Diplomski rad - Backend deo

## Struktura
### [31.08.2020.]
1. notification-service - Servis za hendlovanje notifikacije (info, warning)
2. post-service - Servis zaduzen za rad sa postovima (kreiranje, azuriranje, brisanje, dovlacenje)
3. event-details-service - Servis zaduzen za detalje eventa (bine, izlazi u slucaju opasnosti, ...)


### [21.06.2020.]
1. iam - Servis za upravljanje korisnicima (kreiranje, login, autentikacija, ...)
2. ticket-seller - Servis za prodaju karata (prodaja, dobavljanje karata korisnika, i otkazivanje)
3. Orchestration-service - Glavni servis, poseduje endpointe koje klijent (Android app) gadja
4. docker-compose-base.yml - konfiguracioni fajl putem kog se pokrecu postgres servis i NATS servis
5. docker-compose.yml - konfiguracioni fajl putem kog se pokrecu iam, ticket-seller i orchestration servisi
 

## Opis
### Generalno
Backend deo prakticnog dela diplomskog rada. Sastoji se od 3 mikroservisa koji komuniciraju putem NATS sistema za razmenu poruka. Svaki od mikroservisa je dockerizovan. Kontejneri se pokrecu pomocu `docker-compose.yml` konfiguracionog fajla.
Docker Compose je alat za definisanje i pokretanje vise kontejnerskih Docker aplikacija. Unutar docker-compose-a servisi mogu da pristupe NATS i Postgres servisima preko docker mreze, u projektu nazvanom `diplomski`. Svaki servis je zaduzen za svoje stanje. Jedan servis ne moze da direktno pristupi bazi podataka drugom servisu.

### Orchestration-service
Servis `Orchestration-service` je glavni servis koji prihvata HTTP zahteve sa klijentske strane, i vrsi koordinaciju izvrsenja tih zahteva. Koordinacija se vrsi slanjem poruka preko NATS sistema odgovarajucim servisima pomocu **Request-Reply** obrasca.

 Servis sadrzi po jednu **Proxy** klasu za svaki servis, preko koje salje zahteve ostalim servisima.
 Jedna cela koordinacija nekog klijentskog zahteva se naziva **Saga**. Saga se sastoji od vise **transakcija**, gde jedna transakcija predstavlja zahtev ka jednom servisu.

 U slucaju da u toku izvrsenja sage neka transakcija se ne izvrsi iz bilo kog razloga, pokrece se mehanizam **kompenzacije**, odnosno vracanje stanja svih prethodno uspesnih transakcija u stanje pre izvrsenja transackija. 

### Iam

Servis `Iam` je servis koji je zaduzen za upravljanje korisnicima (kreiranje, azuriranje, brisanje, autentikacija korisnika). Servis pomocu `MessageSubscriber` klase pretplacuje se na **topic**-e, na koje ce da osluskuje za poruke koje salje  `Orchestration-service` servis. Za svaki topic postoji po jedna `MessageHandler` klasa koja zna sta treba da se uradi sa porukom koju primi. Nakon sto zavrsi, rezultat pakuje u odgovarajucem formatu i salje nazad. `MessageHandlerManager` klasa vrsi povezivanje `MessageHahndler` klase na odgovarajuci topic.

### Ticket-Seller

Servis `Ticket-Seller` je zaduzen za prodaju karata. Nacin obradjivanja poruka je isti kao sto je opisan u Iam servisu.

### Notification-Service

Service `Notification-Service` je zaduzen za prosledjivanje notifikacija korisnicima kao i podataka koji su vezani za notifikaciju. Nacin obradjivanja poruka je isti kao sto je opisan u Iam servisu.

### Post-Service 

Servis `Post-Service` je zaduzen za obradjivanje postova koji kreiraju korisnici (kreiranje, azuriranje, brisanje), kao i za skladistenje slika koje idu uz postove. Nacin obradjivanja poruka je isti kao sto je opisan u Iam servisu.

### Event-Details-Service

Servis `Event-Details-Service` je zaduzen za cuvanje detalja oko dogadjaja (eventa). Cuvaju se nazivi objekata na eventu, kao npr. bina (stage) ili izlaz (exit), se podaci o lokacijama na mapi, a u slucaju bine, cuva se i raspored izvodjaca koji nastupaju na toj bini. Nacin obradjivanja poruka je isti kao sto je opisan u Iam servisu.
