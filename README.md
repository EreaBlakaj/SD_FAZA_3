
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

#### Pershkrimi i programit ndahet ne disa pjese kryesore:

##### Metoda e Enkriptimit AES:
**Qëllimi**: Enkripton një mesazh duke përdorur algoritmin AES.
**Parametrat**:
- message: Mesazhi i tekstit të pastër që do të enkriptohet.
- key: Çelësi simetrik AES për enkriptim.
**Procesi**: Krijon një Cipher për AES dhe enkripton mesazhin me çelësin e dhënë.

##### Gjenerimi i Çiftit të Çelësave RSA:
**Qëllimi**: Gjeneron një çift çelësash RSA (privat dhe publik).
**Procesi**: Përdor KeyPairGenerator për të gjeneruar një çift çelësash RSA me madhësi 2048 bit.

##### Nënshkrimi i Mesazhit:
**Qëllimi**: Nënshkruan një mesazh duke përdorur çelësin privat RSA.
**Parametrat**:
- message: Mesazhi që do të nënshkruhet.
- privateKey: Çelësi privat RSA.
**Procesi**: Krijon një Signature për SHA256withRSA, nënshkruan mesazhin dhe e kthen nënshkrimin në formatin Base64.

##### Hash-i i Sekretit të Përbashkët:
**Qëllimi**: Krijon një çelës AES nga sekreti i përbashkët i shkëmbyer.
**Parametrat**:
- sharedSecret: Sekreti i përbashkët i shkëmbyer.
**Procesi**: Krijon një hash SHA-256 nga sekreti i përbashkët dhe përdor 16 bajtet e para të hash-it si çelës AES.

##### Hash-i i Mesazhit:
**Qëllimi**: Gjeneron një hash SHA-256 nga një mesazh.
**Parametrat**:
- message: Mesazhi që do të hashohet.
**Procesi**: Krijon një hash SHA-256 nga mesazhi i dhënë.

##### Metoda kryesore:
Metoda kryesore (main) është aty ku ekzekutohet logjika kryesore e programit. Ajo përmban hapat e nevojshëm për të krijuar një server që përdor protokollin Diffie-Hellman për të shkëmbyer një çelës sekret dhe RSA për të nënshkruar mesazhet.



### Si Funksionon GreetingClient?

GreetingClient është një aplikacion klient-server që përdor Diffie-Hellman për të gjeneruar një çelës sekret të përbashkët dhe RSA për nënshkrimin dhe verifikimin e mesazheve. Ky klient lidhet me një server, ndan parametrat për Diffie-Hellman, verifikon nënshkrimin e mesazheve dhe më pas deshifron mesazhin e marrë nga serveri. Ja se si funksionon ky klient në detaje:

**Funksionet Kryesore:**

- decryptAES: Dekripton një mesazh të enkriptuar me AES duke përdorur një çelës specifik.
- verifySignature: Verifikon nënshkrimin digjital të një mesazhi duke përdorur çelësin publik të serverit.
- getPublicKeyFromBase64: Konverton një çelës publik nga formati Base64 në një objekt PublicKey.
- hashSharedSecret: Hashon çelësin e përbashkët Diffie-Hellman për të krijuar një çelës 128-bitësh për AES.
- hashMessage: Krijon një hash të mesazhit duke përdorur SHA-256.

1. Klienti përcakton parametrat p, g, dhe çelësin privat a për algoritmin Diffie-Hellman. Ai lidhet me serverin në portin 8088.
2. Klienti dërgon parametrat p, g, dhe çelësin publik A të llogaritur te serveri.
3. Klienti merr çelësin publik B nga serveri dhe llogarit çelësin e përbashkët Adash.
4. Klienti merr mesazhin e enkriptuar, nënshkrimin digjital, dhe çelësin publik të serverit.
5. Klienti verifikon nënshkrimin e mesazhit duke përdorur çelësin publik të serverit. Nëse nënshkrimi është i vlefshëm, klienti krijon çelësin simetrik për AES nga çelësi i përbashkët dhe dekripton mesazhin e enkriptuar.

### Integriteti dhe Mos-Mohimi i Mesazheve

Integriteti i mesazhit sigurohet duke hash-uar mesazhin para nënshkrimit. Mos-mohimi arrihet duke përdorur nënshkrimet RSA, duke siguruar që dërguesi nuk mund të mohojë autenticitetin e mesazhit.

### Përdorimi dhe funksionet e librarive:

Në detyrën tonë janë përdorur disa librari të gatshme që ofrojnë funksione të ndryshme për të mbështetur kriptografinë, komunikimin përmes rrjetit, dhe manipulimin e të dhënave. Këtu janë libraritë kryesore të përdorura dhe funksionet që kryejnë ato:

#### java.net:

- **ServerSocket**: Kjo klasë përdoret nga serveri për të pritur lidhjet nga klientët në një port të caktuar.
- **Socket**: Kjo klasë përdoret nga klienti për të krijuar një lidhje me serverin dhe nga serveri për të komunikuar me klientin.
- **InetAddress:** Kjo klasë përdoret për të përfaqësuar një adresë IP.
- **SocketException dhe SocketTimeoutException:** Këto klasa përdoren për të trajtuar gabimet dhe kohëzgjatjet e lidhjeve të rrjetit.

#### java.io:

- **DataInputStream dhe DataOutputStream**: Këto klasa përdoren për të lexuar dhe shkruar të dhëna primitive (si int, double, UTF) në rrjedhën e të dhënave.
- **BufferedReader dhe InputStreamReader**: Këto klasa përdoren për të lexuar të dhëna tekstuale nga një rrjedhë hyrëse.
- **PrintWriter**: Kjo klasë përdoret për të shkruar të dhëna tekstuale në një rrjedhë daljeje.

#### java.security:

- **KeyPair dhe KeyPairGenerator**: Këto klasa përdoren për të krijuar një çift çelësash publik dhe privat për algoritmin RSA.
- **PrivateKey dhe PublicKey**: Këto janë ndërfaqe për çelësat kriptografikë.
- **Signature**: Kjo klasë përdoret për të krijuar dhe verifikuar nënshkrimet dixhitale me algoritmin SHA256withRSA.
- **MessageDigest**: Kjo klasë përdoret për të krijuar hash-e të të dhënave me algoritmin SHA-256.
- **NoSuchAlgorithmException dhe InvalidKeyException**: Këto janë përjashtime që trajtojnë gabimet gjatë përdorimit të algoritmeve kriptografikë dhe çelësave të pavlefshëm.

#### javax.crypto:

- **Cipher**: Kjo klasë përdoret për të kryer operacionet kriptografike si enkriptimi dhe dekriptimi. Në këtë kod përdoret për algoritmin AES.
- **SecretKeySpec**: Kjo klasë përdoret për të përfaqësuar një çelës sekret për algoritmin AES.

#### java.util.Base64:

- **Base64.Encoder dhe Base64.Decoder**: Këto klasa përdoren për të koduar dhe dekoduar të dhënat në formatin Base64, që është një mënyrë për të paraqitur të dhëna binare si tekst.

#### Funksionet kryesore që kryejnë këto klasa në kod janë:

1. **Enkriptimi dhe dekriptimi me AES**: Përdor Cipher dhe SecretKeySpec për të enkriptuar dhe dekriptuar mesazhe. 
2. **Gjenerimi dhe menaxhimi i çelësave RSA**: Përdor KeyPairGenerator për të krijuar një çift çelësash RSA dhe Signature për të nënshkruar dhe verifikuar mesazhe.
3. **Kodimi dhe dekodimi me Base64**: Përdor Base64 për të koduar dhe dekoduar mesazhe të enkriptuara dhe nënshkrime.
4. **Krijimi i hash-eve**: Përdor MessageDigest për të krijuar hash-e të mesazheve dhe sekreteve të ndara.
5. **Komunikimi përmes rrjetit**: Përdor ServerSocket dhe Socket për të krijuar dhe menaxhuar lidhjet ndërmjet klientit dhe serverit.


### Ekzekutimi i aplikacionit

Më poshtë kemi paraqitur ekzekutimin e aplikacionit që kemi krijuar, dhe rezultatet që dalin në terminal. Kemi implementuar edhe Debug Prints për të treguar shkëmbimin korrekt të të dhënave mes serverit dhe klientit.

![image](https://github.com/EreaBlakaj/SD_FAZA_3/assets/120092128/a32260f1-488d-477e-86e2-5b03ad37ace9)


### Punuan:

##### _Erald Keka_

##### _Erea Blakaj_

##### _Era Sheqiri_

##### _Erëza Temaj_
