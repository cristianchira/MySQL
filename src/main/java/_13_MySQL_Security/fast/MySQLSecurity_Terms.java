// Începător: pachetul ține termenii prin care separi citirea privilegiilor de scrierea lor.
// Producție: package _13_MySQL_Security.fast. Fără linie, definițiile nu au folder.
// Expert: pachetul nu înlocuiește schema mysql și nu copiază folderul GRANT.
package _13_MySQL_Security.fast;

// Începător: termenii întorc propozițiile deja scrise în clasa de topic.
// Producție: import _13_MySQL_Security.MySQL_Security. Fără linie, metodele nu pot preda SHOW GRANTS.
// Expert: importul nu deschide sesiunea și nu ocolește Access denied primit de root în tur.
import _13_MySQL_Security.MySQL_Security;

/*
🟢 Începător
    Ce este            glosarul care spune cine verifică propoziția și unde stau drepturile
    De ce e nevoie     fără termeni, SHOW GRANTS și GRANT rămân aceeași acțiune
🟡 Producție
    Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
🔴 Staff
    Unde               fiecare metodă întoarce textul termenului, fără să deschidă o sesiune
Pași                   1. verificarea cererii
                       2. contul user@host
                       3. tabelele de grant
                       4. listarea
                       5. reîncărcarea depreciată
*/
// Începător: clasa ține termenii prin care separi citirea drepturilor de scrierea lor.
// Producție: metodele întorc SHOW GRANTS, UPDATE pe mysql.user sau FLUSH PRIVILEGES. Dacă clasa dispare, exemplele din definiții nu mai au metodă.
// Expert: glosarul nu predă TLS și nu predă conexiunea criptată. Nu garantează că un rol din SHOW GRANTS este activ.
public final class MySQLSecurity_Terms
// Începător: de aici încep termenii despre verificarea privilegiilor.
// Producție: fără acoladă, metodele care întorc propoziția nu au clasă.
// Expert: acolada nu citește tabelele de grant în memorie.
{

    /*
    Definiția unei verificări de cerere (request verification):
           După conexiune, serverul verifică fiecare propoziție pe copia din memorie a privilegiilor.

    Exemplu:
           MySQLSecurity_Terms.explainRequestVerification()
           Fără privilegiul din copie, rândurile aplicației rămân neatinse.
    */
    /*
    🟢 Începător
        Ce este            verificarea pe care serverul o face la fiecare propoziție, după ce a acceptat conexiunea
        De ce e nevoie     fără ea, un cont autentificat pare liber să citească orice tabelă
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
    🔴 Staff
        Unde               etapa 2 citește copia din memorie, nu discul, la fiecare cerere
    Pași                   1. întoarce showGrantsAtOffice
    */
    // Începător: metoda dă propoziția prin care vezi ce drepturi va consulta verificarea pentru joe de la birou.
    // Producție: întoarce SHOW GRANTS FOR 'joe'@'office.example.com'; . Dacă dispare, definiția nu mai are exemplu.
    // Expert: nu este etapa 1, care acceptă conexiunea. Nu garantează privilegiul: lista poate să nu conțină SELECT.
    public static String explainRequestVerification()
    // Începător: înăuntru pleacă citirea drepturilor care vor fi verificate.
    // Producție: fără acoladă, SHOW GRANTS nu iese din metodă.
    // Expert: acolada nu caută în tables_priv și nu acordă un privilegiu global.
    {
        // Începător: înapoi pleacă cererea pe care serverul o poate executa ca să arate drepturile.
        // Producție: return MySQL_Security.showGrantsAtOffice() predă SHOW GRANTS FOR 'joe'@'office.example.com'; . Fără linie, termenul nu are propoziție.
        // Expert: apelul nu este SELECT CONNECTION_ID(). Verificarea de aici este pe privilegii, nu pe firul conexiunii. Un rol din listă rămâne inactiv.
        return MySQL_Security.showGrantsAtOffice();
    // Începător: termenul verificării se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu pornește partial_revokes.
    }

    /*
    Definiția unui cont MySQL (account):
           Contul este numele împreună cu hostul. Același nume la alt host este alt cont.

    Exemplu:
           MySQLSecurity_Terms.explainAccount()
           joe de la birou nu împrumută lista lui joe de acasă.
    */
    /*
    🟢 Începător
        Ce este            perechea nume și host pentru care se țin drepturile
        De ce e nevoie     fără host, două locuri diferite par un singur drept
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'home.example.com';
    🔴 Staff
        Unde               al doilea SHOW GRANTS citește alt rând decât cel de la birou
    Pași                   1. întoarce showGrantsAtHome
    */
    // Începător: metoda dă citirea celuilalt cont, cel de acasă.
    // Producție: întoarce SHOW GRANTS FOR 'joe'@'home.example.com'; . Dacă dispare, termenul de cont nu mai are hostul al doilea.
    // Expert: un GRANT către 'joe'@'%' nu este acest cont. Potrivirea de la conectare poate alege un rând mai specific decât wildcard-ul.
    public static String explainAccount()
    // Începător: înăuntru pleacă lista contului de acasă.
    // Producție: fără acoladă, SHOW GRANTS pentru home nu are metodă în glosar.
    // Expert: acolada nu unește cele două hosturi.
    {
        // Începător: serverul poate întoarce alte drepturi decât la birou.
        // Producție: return MySQL_Security.showGrantsAtHome() predă SHOW GRANTS FOR 'joe'@'home.example.com'; . Fără linie, contul nu are propoziție.
        // Expert: linia nu este USER(). Nu spune ce cont a autentificat sesiunea care o trimite; spune ce este scris pentru home.example.com.
        return MySQL_Security.showGrantsAtHome();
    // Începător: termenul de cont se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu creează contul. CREATE USER rămâne în alt folder.
    }

    /*
    Definiția unor tabele de grant (grant tables):
           Privilegiile stau în schema mysql. Serverul decide pe copia lor din memorie.

    Exemplu:
           MySQLSecurity_Terms.explainGrantTables()
           Un UPDATE direct nu schimbă copia până la reîncărcare sau repornire.
    */
    /*
    🟢 Începător
        Ce este            locul de pe disc în care stau drepturile, separat de copia pe care o folosește serverul
        De ce e nevoie     fără deosebire, un UPDATE pare deja dreptul activ
    🟡 Producție
        Cum se scrie       UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey';
    🔴 Staff
        Unde               discul se poate schimba; etapa 2 rămâne pe memorie până la reîncărcare
    Pași                   1. întoarce directGrantTableUpdate
    */
    // Începător: metoda dă propoziția care scrie în tabela de conturi și nu publică dreptul.
    // Producție: întoarce UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; . Dacă dispare, termenul nu mai are eșecul.
    // Expert: calea directă este tratată ca depreciată în 9.7. Select_priv nu intră într-o sesiune deja deschisă nici după reîncărcare.
    public static String explainGrantTables()
    // Începător: înăuntru pleacă scrierea care ocolește GRANT.
    // Producție: fără acoladă, UPDATE pe mysql.user nu iese din glosar.
    // Expert: acolada nu reîncarcă global_grants.
    {
        // Începător: înapoi pleacă propoziția care poate schimba discul și lasă verificarea veche.
        // Producție: return MySQL_Security.directGrantTableUpdate() predă UPDATE mysql.user SET Select_priv = 'Y' WHERE User = 'jeffrey'; . Fără linie, tabelele de grant nu au exemplu.
        // Expert: user și global_grants țin privilegiul global. tables_priv ține privilegiul de tabelă, care după reîncărcare intră la cererea următoare, nu la o conexiune nouă.
        return MySQL_Security.directGrantTableUpdate();
    // Începător: termenul tabelelor de grant se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu pune Select_priv înapoi pe 'N'.
    }

    /*
    Definiția unei listări de privilegii (SHOW GRANTS):
           SHOW GRANTS citește privilegiile scrise pentru contul numit și nu le acordă.

    Exemplu:
           MySQLSecurity_Terms.explainShowGrants()
           Un rol din listă nu este încă activ.
    */
    /*
    🟢 Începător
        Ce este            propoziția care arată drepturile scrise, fără să le dea
        De ce e nevoie     ca să nu o folosești când privilegiul lipsește și trebuie scris
    🟡 Producție
        Cum se scrie       SHOW GRANTS FOR 'joe'@'office.example.com';
    🔴 Staff
        Unde               rezultatul poate conține o linie REVOKE când partial revoke a tăiat o schemă
    Pași                   1. întoarce showGrantsAtOffice
    */
    // Începător: metoda dă listarea drepturilor pentru contul de la birou.
    // Producție: întoarce SHOW GRANTS FOR 'joe'@'office.example.com'; . Dacă dispare, termenul de listare nu mai are propoziție.
    // Expert: un privilegiu din listă este în vigoare; un rol nu este. Linia nu înlocuiește SHOW CREATE USER, care arată proprietățile contului, nu privilegiile.
    public static String explainShowGrants()
    // Începător: înăuntru pleacă listarea.
    // Producție: fără acoladă, SHOW GRANTS nu iese.
    // Expert: acolada nu face commit implicit.
    {
        // Începător: înapoi pleacă cererea de listă, nu un drept nou.
        // Producție: return MySQL_Security.showGrantsAtOffice() predă SHOW GRANTS FOR 'joe'@'office.example.com'; . Fără linie, listarea nu are text.
        // Expert: cu partial_revokes oprit, un REVOKE pe schemă după un grant global dă ERROR 1141 și această listă nu capătă linia REVOKE. Linia nu filtrează un rând.
        return MySQL_Security.showGrantsAtOffice();
    // Începător: termenul listării se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu activează rolul implicit.
    }

    /*
    Definiția unei reîncărcări de privilegii (FLUSH PRIVILEGES):
           FLUSH PRIVILEGES recitește tabelele de grant. În 9.7 este depreciat și face commit implicit.

    Exemplu:
           MySQLSecurity_Terms.explainFlushPrivileges()
           Nu este necesar după GRANT.
    */
    /*
    🟢 Începător
        Ce este            propoziția care pune din nou în memorie drepturile de pe disc
        De ce e nevoie     ca să nu o pui după GRANT, pentru că nu ea publică privilegiul
    🟡 Producție
        Cum se scrie       FLUSH PRIVILEGES;
    🔴 Staff
        Unde               commit implicit; privilegiul global static așteaptă o conexiune nouă
    Pași                   1. întoarce reloadGrantTables
    */
    // Începător: metoda dă reîncărcarea, nu acordarea unui drept.
    // Producție: întoarce FLUSH PRIVILEGES; . Dacă dispare, termenul nu mai are propoziția depreciată.
    // Expert: cere RELOAD sau privilegiul depreciat FLUSH_PRIVILEGES. Cu --skip-grant-tables, abia ea pornește verificarea. Nu este necesară după GRANT.
    public static String explainFlushPrivileges()
    // Începător: înăuntru pleacă reîncărcarea.
    // Producție: fără acoladă, FLUSH PRIVILEGES nu iese din glosar.
    // Expert: acolada nu golește cache-ul caching_sha2_password; o face serverul dacă primește propoziția.
    {
        // Începător: serverul recitește drepturile și poate închide tranzacția deschisă.
        // Producție: return MySQL_Security.reloadGrantTables() predă FLUSH PRIVILEGES; . Fără linie, reîncărcarea nu are text.
        // Expert: FLUSH este pe lista de commit implicit. Privilegiul de tabelă intră la cererea următoare, cel de bază la USE, cel global static la conexiunea nouă.
        return MySQL_Security.reloadGrantTables();
    // Începător: termenul reîncărcării se încheie aici.
    // Producție: fără închidere, metoda nu compilează.
    // Expert: închiderea nu scoate avertismentul de depreciere din 9.7.
    }
// Începător: glosarul se termină aici.
// Producție: fără închidere, cele cinci metode nu compilează.
// Expert: închiderea nu șterge definițiile și nu mută GRANT în acest fișier.
}
