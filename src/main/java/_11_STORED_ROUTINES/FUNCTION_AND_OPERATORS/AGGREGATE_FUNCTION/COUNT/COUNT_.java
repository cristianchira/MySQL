package _11_STORED_ROUTINES.FUNCTION_AND_OPERATORS.AGGREGATE_FUNCTION.COUNT;

// https://www.udemy.com/course/sql-mysql-for-data-analytics-and-business-intelligence/learn/lecture/8376506#questions

// https://dev.mysql.com/doc/refman/8.0/en/counting-rows.html



/*                                         COUNT()   -- aggregate function


    ○ SELECT COUNT(column_name)
      FROM table_name;


    ○ they gather data from many rows of a table,

                    * then aggregate it into a single value

                    * applicable to both numeric and non-numeric data

                    * you shouldn’t leave white space before opening the parentheses

                     * They ignore null values unless told not to.


                    INPUT                                     OUTPUT

   ○ the information contained                   ○ the single value they provide
          in multiple rows


                          COUNT(DISTINCT column_name)


    ○ helps us find the number of times unique values

                   * are encountered in a given column


                                          COUNT(*)


    ○ Returns the number of all rows of the table,

                  * values NULL included


                                   Why do these functions exist?


    ○ they are a response to the information requirements of a

                   * company’s different organizational levels

                   * top management executives are typically interested in

                                 - summarized figures and rarely in detailed data



                                                  SQL


    ○ SELECT species, sex, COUNT(*) FROM pet
       WHERE species = 'dog' OR species = 'cat'
       GROUP BY species, sex;




 */


/*
TOPIC:
    COUNT(*) numără rândurile, inclusiv pe cele cu NULL. COUNT(sex) numără valorile nenule din coloană. COUNT(DISTINCT species) numără valorile diferite. Niciunul nu șterge rândul.
FAMILIE:
    Routines
VERSION:
    Java 27, MySQL 9.7, Connector/J 26.10.0. Portul 3306 ascultă. root@localhost a primit Access denied (using password: NO). Efectul pe server rămâne NERULAT AICI.
INTENT:
    Din pet rămâne un număr pe grupul species și sex, nu lista de animale. Tabela nu se schimbă.
MECHANISM:
    Serverul execută șirul din countStarGroupsPets. 14.19.1: COUNT este agregat; fără o precizare, agregatele ignoră NULL, iar COUNT(*) este numărătoarea rândurilor. GROUP BY face câte un număr pe grup. Rezultatul se vede la sfârșitul propoziției.
LOOKALIKES:
    COUNT(sex) sare peste NULL și poate fi mai mic decât COUNT(*). SUM(salary) adună valori, nu rânduri. O funcție stocată care dă COUNT nu este agregatul: agregatul nu are CREATE.
BOOK THEORY:
    Manualul 9.7, 14.19.1, deschis în tur: COUNT() numără rândurile întoarse, COUNT(DISTINCT) numără valorile diferite, iar agregatele ignoră NULL dacă nu s-a spus altfel. COUNT().pdf, deschis în tur, are pe primele pagini titlul COUNT(); comentariul din clasă spune că COUNT(*) include NULL, iar COUNT(coloană) nu.
BOOK EXAMPLE:
    countStarGroupsPets
REPO DEMO:
    src/main/java/_11_STORED_ROUTINES/FUNCTION_AND_OPERATORS/AGGREGATE_FUNCTION/COUNT/COUNT_.java
BREAK LAB:
    countColumnDropsNullSex ține SELECT COUNT(sex) FROM pet. Un animal fără sex nu intră în număr. În tur șirul nu a fost trimis.
TRADE-OFFS:
    COUNT(*) când întrebarea este câte rânduri sunt. Aplicația când rândurile sunt deja la client și numărarea nu merită un al doilea SELECT.
ANTI-PATTERNS:
    COUNT(sex) folosit ca număr de animale, când sex poate fi NULL.
STAFF QUESTIONS:
    Întrebările 1–5 de mai jos.
NOVEL TRANSFER:
    Câte colete sunt pe raft este COUNT(*). Câte au o greutate scrisă este COUNT(weight). Un colet fără greutate rămâne pe raft și iese doar din al doilea număr.
SPACED REVIEW:
    O zi: 21 octombrie 2026, COUNT(*) numără rândul. Șapte zile: 27 octombrie 2026, COUNT(sex) sare peste NULL. Treizeci de zile: 19 noiembrie 2026, DISTINCT numără valorile, nu rândurile.

1. Enunț: Ce problemă rezolvă COUNT(*) cu GROUP BY species, sex?
   Capcană: Numără coloanele din pet și scrie numărul înapoi în tabelă.
   Cheie: countStarGroupsPets lasă câte un număr în rezultat pentru fiecare pereche species și sex, la câinii și pisicile filtrate de WHERE. pet rămâne. Nu există CREATE.
   Demonstrație: countStarGroupsPets
2. Enunț: Ce se întâmplă cu un rând în care sex este NULL?
   Capcană: COUNT(*) îl sare, pentru că agregatele ignoră NULL întotdeauna.
   Cheie: COUNT(*) îl numără. countColumnDropsNullSex, adică COUNT(sex), nu îl numără. 14.19.1 spune că agregatele ignoră NULL dacă nu s-a spus altfel; COUNT(*) este numărarea rândurilor, nu a unei coloane.
   Demonstrație: countColumnDropsNullSex
3. Enunț: De ce COUNT(DISTINCT species) nu este GROUP BY species?
   Capcană: Ambele dau același număr și același plan, deci sunt același mecanism.
   Cheie: DISTINCT în COUNT numără valorile diferite din coloană și lasă o valoare. GROUP BY face grupuri, ca să poți număra rândurile fiecărui grup, ca în countStarGroupsPets. Același număr nu este același rezultat când îți trebuie și sexul grupului.
   Demonstrație: countStarGroupsPets
4. Enunț: Cum eșuează COUNT(sex) când vrei câte animale sunt?
   Capcană: NULL este zero, deci intră în număr ca un animal.
   Cheie: countColumnDropsNullSex nu numără rândul fără sex. Animalul este tot în pet. COUNT(*) este numărul de rânduri. Spațiul dintre COUNT și ( privește modul IGNORE_SPACE la built-in, nu schimbă NULL.
   Demonstrație: countColumnDropsNullSex
5. Enunț: Când numeri în aplicație și când lași COUNT pe server?
   Capcană: Numărul din aplicație este mai corect, pentru că vezi rândurile.
   Cheie: Serverul când nu vrei să muți pet la client. Aplicația când rândurile sunt deja acolo. O funcție stocată când expresia trebuie să aibă nume; COUNT însuși nu cere CREATE.
   Demonstrație: countStarGroupsPets

BEST PRACTICES:
    1. Cazul corect
           -> DECIDE câte rânduri sunt pe specie și sex -> countStarGroupsPets -> SELECT species, sex, COUNT(*) FROM pet WHERE species = 'dog' OR species = 'cat' GROUP BY species, sex -> nu garantează că un NULL din sex a fost scos: COUNT(*) îl păstrează în număr
    2. Eșecul cel mai mic
           -> DECIDE COUNT(sex) ca număr de animale -> countColumnDropsNullSex -> SELECT COUNT(sex) FROM pet -> nu garantează numărul de rânduri
    3. Granița
           -> COUNT este agregatul MySQL. Spațiul dinaintea parantezei este o regulă de built-in, nu o rutină stocată. Java nu numără pet.
    4. Alternativa
           -> COUNT(DISTINCT species) când numeri valorile, nu rândurile. SUM când aduni. Aplicația când setul este deja la client.
    5. Anti-patternul
           -> COUNT(sex) în loc de COUNT(*). Animalul fără sex dispare din număr și rămâne în tabelă.
    6. Dovada
           -> countStarGroupsPets ține COUNT(*). countColumnDropsNullSex ține COUNT(sex). root@localhost a fost refuzat, using password: NO.
    7. Interviu
           -> COUNT(*) numără rândurile. COUNT(coloană) sare peste NULL. GROUP BY face câte un număr pe grup. Tabela nu se schimbă.
*/

public class COUNT_ {

    /*
    🟢 Începător
        Ce este            câte animale sunt, pe specie și pe sex, fără să le ștergi
        De ce e nevoie     fără număr, rămâne lista, nu cifra cerută
    🟡 Producție
        Cum se scrie       SELECT species, sex, COUNT(*) FROM pet WHERE species = 'dog' OR species = 'cat' GROUP BY species, sex
    🔴 Staff
        Unde               WHERE alege rândurile înainte de grup. COUNT(*) numără rândul chiar dacă sex este NULL
    Pași
        1. Filtrează speciile.
        2. Grupează și numără cu COUNT(*).
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează numărarea rândurilor pe grup.
    // Producție: public static String countStarGroupsPets(). Dacă dispare, exemplul din comentariu nu este propoziția gata de trimis.
    // Expert: WHERE nu este HAVING. HAVING ar filtra grupul după ce COUNT a rulat.
    public static String countStarGroupsPets() {
        // Începător: rămân cifre pentru câini și pisici, despărțite și după sex, iar animalele rămân în tabelă.
        // Producție: SELECT species, sex, COUNT(*) FROM pet WHERE species = 'dog' OR species = 'cat' GROUP BY species, sex. Dacă linia dispare, nu mai ai COUNT(*) cu GROUP BY.
        // Expert: Un sex NULL face grupul lui, iar COUNT(*) îl numără. COUNT(sex) pe același grup ar da 0 pentru acel grup.
        return "SELECT species, sex, COUNT(*) FROM pet WHERE species = 'dog' OR species = 'cat' GROUP BY species, sex";
        // Începător: metoda a înapoiat numărarea, nu animalele.
        // Producție: acolada închide countStarGroupsPets. Șirul nu a plecat spre 3306.
        // Expert: Refuzul root@localhost lasă planul NERULAT. Acolada nu este un index pe species.
    }

    /*
    🟢 Începător
        Ce este            numărarea valorilor din sex, nu a rândurilor
        De ce e nevoie     fără ea, pare că orice COUNT vede același lucru
    🟡 Producție
        Cum se scrie       SELECT COUNT(sex) FROM pet
    🔴 Staff
        Unde               NULL din sex nu intră. Rândul rămâne în pet. COUNT(*) l-ar fi numărat
    Pași
        1. Numără coloana, nu steaua.
        2. Nu pune GROUP BY, ca numărul să fie unul singur.
        3. Nu trimite șirul în tur.
    */
    // Începător: metoda păstrează numărul care pierde animalele fără sex.
    // Producție: public static String countColumnDropsNullSex(). Dacă dispare, deosebirea de COUNT(*) rămâne o frază.
    // Expert: COUNT(DISTINCT sex) ar număra valorile diferite, tot fără NULL, și tot nu este numărul de rânduri.
    public static String countColumnDropsNullSex() {
        // Începător: animalele fără sex nu intră în cifră, deși rămân în tabelă.
        // Producție: SELECT COUNT(sex) FROM pet. Dacă linia dispare, nu mai ai COUNT pe coloană.
        // Expert: 14.19.1: agregatul ignoră NULL dacă nu s-a spus altfel. COUNT(*) este numărătoarea rândurilor întoarse, nu această linie.
        return "SELECT COUNT(sex) FROM pet";
        // Începător: metoda a păstrat numărul incomplet.
        // Producție: acolada închide countColumnDropsNullSex. Șirul a rămas în proces.
        // Expert: Nu este o rutină stocată. Nu se pornește cu CALL.
    }
}
