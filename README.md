
# Siguria e të Dhënave/Diffie-Hellman and Digital Signatures Console Application

### Përmbledhje e Projektit

Ky projekt përfshin krijimin e një aplikacioni për komunikim të sigurtë klient-server duke përdorur Java. Aplikacioni përdor protokollin e shkëmbimit të çelësave Diffie-Hellman për të vendosur një sekret të përbashkët dhe nënshkrimet dixhitale për të siguruar integritetin dhe autenticitetin e mesazheve të shkëmbyera midis klientit dhe serverit.

## Shkëmbimi i Çelësave Diffie-Hellman

Algoritmi i shkëmbimit të çelësave Diffie-Hellman është një metodë që përdoret për të shkëmbyer çelësa kriptografikë në mënyrë të sigurt mbi një kanal publik. Ky protokoll lejon dy palë, secila me një vlerë private dhe një vlerë publike përkatëse, të gjenerojnë një sekret të përbashkët pa transmetuar sekretin vetë. Ky sekret i përbashkët mund të përdoret më pas si një çelës për algoritme të kriptimit simetrik si AES.

## Krijimi dhe Verifikimi i Nënshkrimit Digjital

Nënshkrimet digjitale janë teknika kriptografike që përdoren për të siguruar vërtetësinë dhe integritetin e një mesazhi. Ato ofrojnë një mënyrë për të verifikuar që mesazhi është krijuar nga një dërgues i njohur dhe nuk është ndryshuar gjatë transmetimit. Kjo arrihet duke përdorur një çift çelësash: një çelës privat (i njohur vetëm nga nënshkruesi) dhe një çelës publik (i ndarë me këdo që ka nevojë të verifikojë nënshkrimin).

#### Mesazhet midis klientit dhe serverit janë të koduara duke përdorur kriptimin AES, i cili përdor çelësin e përbashkët të gjeneruar nga shkëmbimi i çelësave Diffie-Hellman.

### Implementimi i Serverit

**Serveri:**

- Dëgjon për lidhje nga klientët.
- Merr pjesë në shkëmbimin e çelësave Diffie-Hellman.
- Nënshkruan mesazhet dhe i dërgon ato klientit së bashku me çelësin publik.

### Implementimi i Klientit

**Klienti:**

- Lidhet me serverin dhe merr pjesë në shkëmbimin e çelësave Diffie-Hellman.
- Verifikon nënshkrimin dixhital të serverit.
- Dekripton mesazhet e marra duke përdorur çelësin e përbashkët.

### Përmbledhje e kodit:

#### Kodi i Serverit:

**Gjenerimi i Çelësave**: Serveri gjeneron një palë çelësa RSA për nënshkrimin e mesazheve.
**Shkëmbimi i Çelësave Diffie-Hellman**: Serveri llogarit vlerën publike të tij dhe e shkëmben atë me klientin.
**Kriptimi i Mesazheve**: Serveri kodon një mesazh duke përdorur AES me çelësin e përbashkët.
**Nënshkrimi i Mesazheve**: Serveri nënshkruan mesazhin e hash-uar me çelësin privat RSA.
**Komunikimi**: Serveri dërgon mesazhin e koduar, nënshkrimin dhe çelësin publik te klienti.

#### Kodi i Klientit

**Shkëmbimi i Çelësave Diffie-Hellman**: Klienti llogarit vlerën publike të tij dhe e shkëmben atë me serverin.
**Verifikimi i Nënshkrimit**: Klienti verifikon nënshkrimin e serverit duke përdorur çelësin publik të marrë.
**Dekriptimi i Mesazheve**: Klienti dekripton mesazhin e marrë duke përdorur AES me çelësin e përbashkët.


### Si funksionon GreetingServer?

Ky projekt implementon një aplikacion në anën e serverit për protokollin e shkëmbimit të çelësave Diffie-Hellman, 
i cili vendos një çelës sekret të përbashkët për enkriptimin simetrik. Serveri pret për lidhje nga klientët, 
merr pjesë në shkëmbimin e çelësave dhe dërgon një mesazh të enkriptuar te klienti



#### Pershkrimi i programit ndahet ne disa metoda kryesore:

##### Metoda e Enkriptimit:

Kjo metodë kryen një enkriptim të thjeshtë Caesar në një mesazh të dhënë duk Kjo metodë kryen një enkriptim të thjeshtë Caesar në një mesazh të dhënë duke përdorur një çelës zhvendosjeje.

##### Parametrat:

- message: Mesazhi i tekstit të pastër që do të enkriptohet.
- shiftKey: Çelësi i zhvendosjes që përdoret për enkriptimin Caesar.
**Procesi**: Metoda konverton secilën shkronjë të mesazhit në shkronjën përkatëse në alfabet të zhvendosur nga çelësi i zhvendosjes. Rezultati është një tekst i enkriptuar

##### Metoda kryesore:

- Konfigurimi i Portit: Serveri dëgjon në portin 8088.
- Çelësi i Serverit: Çelësi privat i serverit b është vendosur në 3.
- Vendosja e Lidhjes: Serveri pret për një lidhje nga klienti dhe pastaj pranon lidhjen.
- Shkëmbimi i Çelësave:
Serveri pranon parametrat e klientit p, g dhe A.
Llogarit vlerën e tij publike B dhe ia dërgon klientit.
Llogarit çelësin sekret të përbashkët Bdash.
- Enkriptimi i Mesazhit: Duke përdorur çelësin sekret Bdash, serveri enkripton një mesazh të paracaktuar ("Hellofromtheserver") me enkriptimin Caesar dhe ia dërgon mesazhin e enkriptuar klientit.
- Mbyllja e Lidhjes: Serveri mbyll lidhjen pas dërgimit të mesazhit të enkriptuar.
Ky implementim demonstron një shkëmbim të thjeshtë të çelësave Diffie-Hellman dhe përdor një enkriptim të thjeshtë Caesar për mesazhin. Ai tregon procesin e vendosjes së një çelësi të përbashkët mbi një kanal të pasigurt, i cili më pas mund të përdoret për komunikim të sigurt.



