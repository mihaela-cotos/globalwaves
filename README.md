#### Cotos Mihaela, 322CD
# Proiect GlobalWaves  - Etapa 2

Pentru partea a doua am extins implementarea echipei de POO a etapei 1, adăugând funcționalitățile noi.

### Useri - Factory Pattern
În cadrul acestei etape, am extins funcționalitățile clasei inițiale User, creând cele 3 tipuri de useri :

- useri simpli/normali : au păstrat metodele implementate în etapa 1;
- artiști : utilizator care poate adăuga albume și are propria pagină asociată;
- hosts : utilizator care poate adăuga podcast-uri și are propria pagină asociată.

Am implementat această structură pentru a gestiona funcționalitățile specifice ale fiecărui tip de utilizator.

Pentru a crea instanțe ale diferitelor tipuri de utilizatori, am implementat **Factory Pattern**. Astfel, am creat o fabrică (UserFactory) care generează utilizatori în funcție de tipul specificat.

În UserFactory este declarată metoda create, care este suprascrisă în fiecare clasă specifică (SimpleUserFactory, ArtistFactory, HostFactory) și returnează utilizatorul creat.

### Pages - Strategy Pattern
Am adăugat în aplicație o structură de pagini care permite utilizatorilor să acceseze diferite secțiuni ale platformei.

- HomePage
- LikedContentPage
- ArtistPage
- HostPage

Am implementat sistemul de pagini folosind Strategy Pattern, pornind de la faptul că printCurrentPage trebuie implementat în mod specific pentru fiecare pagină. Astfel, fiecare print are propria pagină care implementează interfața comună PrintPageStrategy și unde implementează strategia sa de printare (ex : HomePageStrategy, LikedContentPageStrategy).

### Other
Celelalte funcționalități/operații care trebuiau implementate le-am creat după modelul celor deja existente din prima etapă.