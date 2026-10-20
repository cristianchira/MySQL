/*
TOPIC:
    Cincizeci de întrebări despre alegerea dintre procedură, funcție, agregat, built-in și operator.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Recunoașterea din SQL sau din eroare, momentul în care se văd rândurile, deosebirea de AVG și de CALL, eșecul 1418 și alegerea față de aplicație.
MECHANISM:
    CREATE PROCEDURE emp_salary salvează rutina. CALL trimite setul. CREATE FUNCTION f_emp_avg_salary întoarce o valoare în SELECT. AVG nu are obiect.
LOOKALIKES:
    Trigger-ul pornește de la scrierea rândului. @v_emp_no este sesiunea. DELIMITER este clientul. COUNT nu este o rutină creată.
BOOK THEORY:
    Manualul 9.7, 27.2, 15.1.22, 15.2.1, 27.9, 14.19.1, 14.4.2 și 14.5, deschise în tur. PDF-urile din folder nu conțin ER_SP_NO_RETSET și nici 1418.
BOOK EXAMPLE:
    Probele din STORED_ROUTINES, nu o metodă din această clasă.
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/STORED_ROUTINES.java
BREAK LAB:
    SELECT fără INTO într-o funcție, sau CALL f_emp_avg_salary(10001), sau WHERE salary = NULL.
TRADE-OFFS:
    AVG când nu vrei obiect. Aplicația când nimeni altcineva nu evaluează expresia. Procedura când trebuie setul sau scrierea.
ANTI-PATTERNS:
    SELECT emp_salary(10001), log_bin_trust_function_creators = 1, CREATE FUNCTION COUNT().
STAFF QUESTIONS:
    Întrebările 1–50 de mai jos. Cheia este scrisă. Nu există main care să o citească singur.
NOVEL TRANSFER:
    Coletul mutat este CALL. Codul de bare este funcție. Numărul de colete este COUNT. Greutatea lipsă nu iese la = NULL.
SPACED REVIEW:
    O zi: întrebările 11–20. Șapte zile: 31–40. Treizeci de zile: 41–50.

Definiția unei rutine stocate (stored routine):
       Rutina stocată este un set de propoziții păstrate pe server. CREATE o pune în schemă. Corpul nu rulează la CREATE.

Exemplu:
       StoredRoutines_Terms.explainStoredRoutine()

Recunoaștere din SQL, EXPLAIN sau eroare:
    1. Enunț: Vezi CREATE PROCEDURE emp_salary(IN p_emp_no INTEGER) BEGIN SELECT ... END. Ce obiect s-a salvat și ce nu s-a citit?
       Capcană: Salariile lui 10001 sunt deja în rezultat, pentru că SELECT-ul este în corp.
       Cheie: S-a salvat procedura. SELECT-ul nu a rulat. Rândurile pleacă la CALL emp_salary(10001). Nu există RETURNS.
    2. Enunț: Clientul primește ERROR 1418 (HY000) la CREATE FUNCTION. Ce lipsește?
       Capcană: Este ER_SP_NO_RETSET, deci funcția a trimis un set.
       Cheie: 27.9: cu binary logging, lipsește DETERMINISTIC, NO SQL sau READS SQL DATA. 1418 nu este setul de rezultate și nu este un CALL.
    3. Enunț: Ce recunoști în ER_SP_NO_RETSET, Not allowed to return a result set from a function?
       Capcană: Procedura emp_salary a fost respinsă pentru că are SELECT.
       Cheie: Funcția a încercat să trimită un set. SELECT fără INTO este legal în procedură și interzis în funcție. 15.1.22.
    4. Enunț: Vezi CALL list_limit și CALL list_limit(). Câte proceduri sunt?
       Capcană: Două, pentru că parantezele schimbă semnătura.
       Cheie: 15.2.1: fără argumente, cele două CALL-uri sunt același apel. () de la CREATE este alt fapt: lista goală este obligatorie la definiție.
    5. Enunț: Vezi SELECT NULL = NULL, NULL <=> NULL. Ce valori ies?
       Capcană: Ambele dau 1, pentru că nulul este egal cu nulul.
       Cheie: 14.4.2: = dă NULL. <=> dă 1. WHERE salary = NULL nu alege rândul. Nu s-a creat o rutină.
    6. Enunț: EXPLAIN nu este în discuție: ai doar CREATE PROCEDURE și niciun CALL. Ce poți spune despre index?
       Capcană: Indexul de pe emp_no este ținut cât există procedura.
       Cheie: Corpul nu a rulat. Nu s-a ales un index și nu s-a ținut un lacăt. Planul apare la CALL, dacă SELECT-ul citește tabela.
    7. Enunț: Ce recunoști în SET @p_salary = 10; CALL emp_raise_marker(@p_salary), iar procedura are INOUT?
       Capcană: Rândul din salaries cu 10 devine 11.
       Cheie: Se schimbă variabila de sesiune. Tabela nu este numită. Fără SET, intrarea era NULL.
    8. Enunț: Vezi CALL emp_info('Bezalel', 'Simmel', @p_emp_no) și apoi SELECT @p_emp_no. Unde este numărul?
       Capcană: În setul de rezultate, pentru că orice procedură trimite rânduri.
       Cheie: OUT a scris în variabila de utilizator. Numărul se citește după ce CALL se întoarce. Un literal pe poziția OUT nu avea unde scrie.
    9. Enunț: Ce recunoști în SELECT AVG(salary) FROM salaries, fără GROUP BY și fără CREATE?
       Capcană: Este f_emp_avg_salary, doar că ai uitat numele.
       Cheie: Este agregatul built-in pe toată tabela. Nu există obiect în schemă și nu există EXECUTE pe o rutină a ta. NULL nu intră în medie.
    10. Enunț: Vezi CREATE FUNCTION COUNT() RETURNS INT RETURN 1. Ce s-a lovit?
        Capcană: Ai înlocuit agregatul, iar COUNT(*) va da 1.
        Cheie: Numele este un built-in. 15.1.22: eroarea de sintaxă apare cu paranteza lipită. COUNT(salary) rămâne agregatul.

Rânduri, index, lacăt și momentul în care se vede efectul:
    11. Enunț: La sfârșitul lui CREATE PROCEDURE emp_salary, ce s-a schimbat în salaries?
        Capcană: Rândurile lui 10001 sunt blocate până la DROP PROCEDURE.
        Cheie: Nimic în rânduri. S-a schimbat schema. Lacătul, dacă planul îl cere, ține pe durata SELECT-ului din CALL, nu cât trăiește rutina.
    12. Enunț: Când vezi media lui f_emp_avg_salary(10001)?
        Capcană: La CREATE, pentru că RETURN este în corp.
        Cheie: Când expresia este evaluată. CREATE nu calculează media și nu face COMMIT pe salaries.
    13. Enunț: Un IN p_emp_no este schimbat cu SET p_emp_no = 0 în corp. Ce vede apelantul după CALL emp_salary(10001)?
        Capcană: Vede 0, pentru că a pasat valoarea prin referință.
        Cheie: 15.1.22: modificarea unui IN nu este vizibilă apelantului. 10001 rămâne. Setul este altceva: rândurile din SELECT.
    14. Enunț: OUT p_emp_no, înainte de SELECT INTO. Ce valoare are în corp?
        Capcană: Are literalul pus în CALL, ca un IN.
        Cheie: Începe cu NULL. @p_emp_no se vede schimbat când CALL se întoarce, dacă ai trecut variabila, nu un literal.
    15. Enunț: INOUT fără SET @p_salary înainte de CALL. Ce iese?
        Capcană: Iese 1, pentru că pleacă de la zero.
        Cheie: Intrarea este NULL. NULL + 1 rămâne NULL. salaries nu crește.
    16. Enunț: COUNT(*) și COUNT(sex) pe un pet cu sex NULL. Ce se întâmplă cu rândul?
        Capcană: Ambele îl sar, pentru că agregatele ignoră NULL.
        Cheie: Rândul rămâne în tabelă. COUNT(*) îl numără. COUNT(sex) nu. Nu se ține un lacăt după ce rezultatul a plecat.
    17. Enunț: SUM(salary) când WHERE nu lasă rânduri. Ce se scrie în salaries?
        Capcană: Se scrie 0, ca să nu se piardă totalul.
        Cheie: Nu se scrie nimic. Comentariul din SUM_ spune că setul fără rânduri dă NULL în rezultat. 14.19.1: NULL din coloană este ignorat, nu adunat ca zero.
    18. Enunț: ROUND(AVG(salary), 2). Ce scară are coloana salary după propoziție?
        Capcană: Două zecimale, pentru că ROUND a rulat pe server.
        Cheie: Scara din rezultat este a valorii rotunjite. Coloana rămâne. ROUND nu grupează; AVG a grupat.
    19. Enunț: COALESCE(dept_name, 'Department name not provided') într-un SELECT. Ce rămâne în departments_dup?
        Capcană: NULL este înlocuit în celulă.
        Cheie: Celula rămâne NULL. Textul este doar în rezultat. UPDATE cu aceeași funcție ar fi scrierea.
    20. Enunț: Procedura conține COMMIT. Funcția, în același loc. Care trece?
        Capcană: Ambele, pentru că sunt rutine stocate.
        Cheie: 15.1.22: procedura poate conține COMMIT. Funcția nu poate face commit sau rollback, explicit sau implicit.

Deosebire de mecanismul vecin:
    21. Enunț: De ce CALL emp_salary(10001) nu este SELECT f_emp_avg_salary(10001)?
        Capcană: Ambele întorc salariul, deci sunt aceeași invocare.
        Cheie: CALL pornește procedura și poate trimite mai multe rânduri. SELECT evaluează funcția și primește o valoare. Funcția nu are OUT.
    22. Enunț: De ce AVG(salary) nu este f_emp_avg_salary?
        Capcană: Funcția este doar un alias, deci cere același CREATE.
        Cheie: AVG nu are obiect și nu primește emp_no. Funcția stocată are RETURNS, READS SQL DATA și privilegiu EXECUTE. Fără GROUP BY, AVG este toată tabela.
    23. Enunț: De ce DELIMITER $$ nu este prima linie din corpul trimis prin JDBC?
        Capcană: Fără el, serverul taie procedura la primul punct și virgulă.
        Cheie: DELIMITER este comanda clientului mysql, ca să lase ; din corp să ajungă la server. JDBC trimite corpul ca o propoziție. Serverul nu execută DELIMITER.
    24. Enunț: De ce trigger-ul nu ține locul lui emp_salary?
        Capcană: Ambele rulează SQL salvat, deci se pornesc cu CALL.
        Cheie: Trigger-ul pornește de la scrierea unui rând. Procedura pornește de la CALL. Trigger-ul nu este EXECUTE pe emp_salary.
    25. Enunț: De ce @v_emp_no = 10001 nu este parametrul IN?
        Capcană: Variabila de utilizator este IN, pentru că ține 10001.
        Cheie: @v_emp_no trăiește pe sesiune și în afara rutinei. IN este copia din procedură, iar schimbarea ei nu se întoarce în variabilă.
    26. Enunț: De ce IFNULL(dept_name, text) nu este COALESCE cu trei argumente, scris la fel?
        Capcană: IFNULL acceptă lista, pentru că este același lucru.
        Cheie: IFNULL are exact două argumente. COALESCE ia primul nenul dintr-o listă. Niciunul nu schimbă rândul într-un SELECT.
    27. Enunț: De ce LEAST(10, 20) nu este MIN(salary)?
        Capcană: Ambele dau minimul din salaries.
        Cheie: LEAST compară argumentele expresiei și nu citește tabela. MIN este agregatul pe grup. GROUP BY emp_no face câte un minim.
    28. Enunț: De ce ROUND(salary, 2) nu este agregatul din folderul în care stă?
        Capcană: Folderul îl face agregat, deci lasă un rând.
        Cheie: ROUND schimbă un număr. Fără AVG, rămâne un rând pe contract. Jumătatea exactă pleacă dinspre zero; aproximativul poate cădea la par.
    29. Enunț: De ce WHERE salary = NULL nu este WHERE salary IS NULL?
        Capcană: = este egalul, deci găsește lipsa.
        Cheie: = NULL nu este adevărat. IS NULL este testul. <=> este egalul care vede două NULL. Niciunul nu este o rutină.
    30. Enunț: De ce CREATE PROCEDURE list_limit BEGIN nu este CALL list_limit?
        Capcană: Parantezele sunt opționale peste tot, deci CREATE-ul trece.
        Cheie: 15.2.1 permite omiterea doar la CALL fără argumente. 15.1.22 cere () la CREATE, chiar dacă lista este goală.

Eșec în producție:
    31. Enunț: SELECT emp_salary(10001) ajunge în producție. Ce primește clientul?
        Capcană: Primește rândurile, pentru că numele este procedura.
        Cheie: Procedura nu este o expresie. Invocarea este CALL. selectOfAProcedure nu citește salaries.
    32. Enunț: Două persoane se numesc la fel, iar emp_info face SELECT INTO în OUT. Ce se rupe?
        Capcană: OUT primește ambele numere, pentru că poate ține mai multe valori.
        Cheie: SELECT INTO într-o variabilă nu primește un set. OUT este o valoare. Mai multe rânduri fac propoziția să eșueze. Nu este un set legal de procedură: ai cerut INTO.
    33. Enunț: log_bin_trust_function_creators = 1 ca să treacă CREATE FUNCTION fără READS SQL DATA. Ce ai câștigat?
        Capcană: Funcția a devenit deterministă și sigură pentru replicare.
        Cheie: 27.9: sare verificarea. Serverul tot nu controlează dacă funcția schimbă date. DETERMINISTIC mincinos poate strica planul. Nu înlocuiește READS SQL DATA.
    34. Enunț: DECLARE stă înainte de BEGIN, ca în scheletul din curs. Ce face serverul?
        Capcană: Declară variabila la fel ca după BEGIN.
        Cheie: Declarația stă în corpul dintre BEGIN și END. Scheletul este marcat HISTORICAL. Exemplul emp_info din același fișier o pune după BEGIN.
    35. Enunț: NO SQL READS SQL DATA pe aceeași funcție. Ce caracteristică ai declarat?
        Capcană: Ai spus și că nu există SQL, și că citește, iar serverul le împacă.
        Cheie: 15.1.22: caracteristica de acces este una. SELECT din corp cere READS SQL DATA. NO SQL ar spune că nu există propoziții SQL.
    36. Enunț: COUNT(sex) este raportat ca număr de animale, iar unele nu au sex. Ce cifră lipsește?
        Capcană: Niciuna: NULL este zero și intră în număr.
        Cheie: COUNT(sex) sare peste NULL. Animalul rămâne în pet. COUNT(*) este numărul de rânduri.
    37. Enunț: SUM(from_date) este folosit ca să adune vechimea. Ce iese?
        Capcană: O dată, suma calendarelor.
        Cheie: 14.19.1: SUM pe temporal convertește la număr și pierde ce urmează primului caracter nenumeric. Vechimea nu este această sumă.
    38. Enunț: UPDATE departments_dup SET dept_name = COALESCE(dept_name, text), ca să „afișezi” numele. Ce s-a pierdut?
        Capcană: Nimic: este același COALESCE ca în SELECT.
        Cheie: NULL a fost scris peste. Următorul client nu mai vede lipsa. SELECT lăsa celula.
    39. Enunț: CALL emp_salary(@p_emp_no) și apoi SELECT @p_emp_no, așteptând salariul. Ce este în variabilă?
        Capcană: Salariul, pentru că ai folosit o variabilă de utilizator.
        Cheie: Parametrul este IN. Variabila ține intrarea. Salariul, dacă a plecat, este în set, nu în @p_emp_no.
    40. Enunț: Funcția este declarată DETERMINISTIC și citește salaries, care se schimbă. Ce nu verifică serverul?
        Capcană: Refuză CREATE, pentru că vede SELECT-ul.
        Cheie: 15.1.22 și 27.9: sinceritatea nu este verificată. Optimizerul poate refolosi un plan greșit. READS SQL DATA spune că citește; DETERMINISTIC spune că aceeași intrare dă aceeași ieșire.

Trade-off, inclusiv când rămâi în aplicație:
    41. Enunț: Trei clienți trebuie să citească aceleași salarii pentru un emp_no, fără drept pe tabele. Ce alegi?
        Capcană: Le trimiți JOIN-ul, pentru că este mai simplu decât o rutină.
        Cheie: Procedura, cu EXECUTE și fără dreptul pe employees. CREATE nu ține loc de CALL. Funcția, doar dacă rezultatul este o valoare, nu setul.
    42. Enunț: Un singur ecran calculează media și nimeni altcineva nu o cheamă. Unde stă calculul?
        Capcană: În CREATE FUNCTION, altfel nu este corect.
        Cheie: În aplicație, dacă rândurile sunt deja acolo și nu îți trebuie EXECUTE. AVG pe server, dacă nu vrei să muți salaries. Funcția stocată este de prisos.
    43. Enunț: Vrei media tuturor salariilor, fără un nume în schemă. Ce nu creezi?
        Capcană: Creezi f_emp_avg_salary fără parametru.
        Cheie: Nu creezi rutină. SELECT AVG(salary) FROM salaries. Obiectul ar cere CREATE ROUTINE și DROP mai târziu, fără un câștig.
    44. Enunț: Vrei să schimbi un salariu și să lași schimbarea vizibilă altui client. INOUT sau UPDATE?
        Capcană: INOUT, pentru că se numește p_salary.
        Cheie: UPDATE pe salaries. INOUT schimbă @p_salary în sesiune. Celălalt client nu vede variabila ta.
    45. Enunț: Formatul unei date ține de ecran, nu de o regulă comună. DATE_FORMAT sau aplicația?
        Capcană: DATE_FORMAT, pentru că data stă în server.
        Cheie: Aplicația, când textul nu trebuie să fie același în toate propozițiile. DATE_FORMAT când toți clienții trebuie să vadă același șir din SQL. Nu crea SYSDATE ca funcție a ta.
    46. Enunț: Același corp, LIMIT 1000, fără emp_no. Pui IN sau lași ()?
        Capcană: Pui IN p_unused, ca să pară parametrizat.
        Cheie: CREATE PROCEDURE list_limit(). CALL poate fi cu sau fără paranteze. IN când valoarea chiar se schimbă între apeluri.
    47. Enunț: Numărul de colete de pe un raft trebuie să fie o cifră, nu o rutină. COUNT sau CREATE FUNCTION?
        Capcană: CREATE FUNCTION count_bins(), ca să fie în acest folder.
        Cheie: COUNT(*) cu GROUP BY raft. Nu există EXECUTE pe un obiect al tău. NULL într-o coloană nu scoate rândul din COUNT(*).
    48. Enunț: Codul de bare trebuie să apară într-un SELECT alături de alte coloane, pentru toți clienții. Procedură sau funcție?
        Capcană: Procedură, pentru că este salvat pe server.
        Cheie: Funcție, cu RETURNS, chemată în expresie. Procedura nu stă în lista de SELECT. Dacă codul este doar DATE_FORMAT sau un calcul built-in, nu salva nici funcția.
    49. Enunț: Greutatea lipsă trebuie să rămână lipsă, dar ecranul să arate un text. SELECT sau UPDATE?
        Capcană: UPDATE cu COALESCE, ca să nu mai vezi NULL.
        Cheie: SELECT cu COALESCE sau IFNULL. UPDATE ar șterge lipsa pentru toți. IFNULL are două argumente; COALESCE poate avea mai multe.
    50. Enunț: Un calcul folosește un sistem din afara bazei, pe care propoziția nu îl vede. Rutină sau aplicație?
        Capcană: Rutină, pentru că 27.2 spune că banca ține operațiile în proceduri.
        Cheie: Aplicația, când serverul nu vede cealaltă parte. Rutina când regula este doar pe rândurile și privilegiile din MySQL. 27.2 mută lucrul pe server și nu aduce un sistem pe care propoziția nu îl poate citi.

*/
// Începător: banca stă în comentariu, cu cheia scrisă, fără un main care să o citească.
// Producție: package _11_STORED_ROUTINES.fast. Clasa nu trimite SQL.
// Expert: Nu copia întrebările pe COUNT singur sau pe un trigger. Subiectul este alegerea rutinei.
package _11_STORED_ROUTINES.fast;

// Începător: clasa nu pornește un apel și nu creează emp_salary.
// Producție: public final class StoredRoutines_Staff_Interview. Fără main de recall.
// Expert: Cele 50 rămân aici. O întrebare nouă se adaugă, nu înlocuiește una corectă.
public final class StoredRoutines_Staff_Interview {
    // Începător: nu există o propoziție în corp.
    // Producție: acolada închide StoredRoutines_Staff_Interview. Nu se adaugă un main care să tipărească cheia.
    // Expert: Probele stau în STORED_ROUTINES și în copii. Golul de aici nu șterge cele 50.
}
