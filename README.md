##GreetingServer
Ky projekt implementon një aplikacion në anën e serverit për protokollin e shkëmbimit të çelësave Diffie-Hellman, 
i cili vendos një çelës sekret të përbashkët për enkriptimin simetrik. Serveri pret për lidhje nga klientët, 
merr pjesë në shkëmbimin e çelësave dhe dërgon një mesazh të enkriptuar te klienti

##Si behet ekzekutimi i programit?
1.Perpilojm Programin:
Hapni një terminal apo komandë, navigoni te direktoria që përmban file GreetingServer.java dhe përpiloni programin duke përdorur komandën: javac GreetingServer.java
2.Ekzekutojm Programin:
Pas përpilimit të suksesshëm, ekzekutojm programin e serverit me komandën: java GreetingServer
Serveri do të fillojë dhe do të dëgjojë në portin 8088 për një lidhje nga klienti.
3.Lidhja me Klientin:
Sigurohemi qe te kemi programin klient që mund të lidhet me këtë server, të dërgojë parametrat e nevojshëm (p, g, A) dhe të pranojë mesazhin e enkriptuar.

##Pershkrimi i programit ndahet ne disa metoda kryesore:

1. Metoda e Enkriptimit: Kjo metodë kryen një enkriptim të thjeshtë Caesar në një mesazh të dhënë duk Kjo metodë kryen një enkriptim të thjeshtë Caesar në një mesazh të dhënë duke përdorur një çelës zhvendosjeje.
Parametrat:
•message: Mesazhi i tekstit të pastër që do të enkriptohet.
•shiftKey: Çelësi i zhvendosjes që përdoret për enkriptimin Caesar.
Procesi: Metoda konverton secilën shkronjë të mesazhit në shkronjën përkatëse në alfabet të zhvendosur nga çelësi i zhvendosjes. Rezultati është një tekst i enkriptuar

2.Metoda kryesore:
•Konfigurimi i Portit: Serveri dëgjon në portin 8088.
•Çelësi i Serverit: Çelësi privat i serverit b është vendosur në 3.
•Vendosja e Lidhjes: Serveri pret për një lidhje nga klienti dhe pastaj pranon lidhjen.
•Shkëmbimi i Çelësave:
Serveri pranon parametrat e klientit p, g dhe A.
Llogarit vlerën e tij publike B dhe ia dërgon klientit.
Llogarit çelësin sekret të përbashkët Bdash.
•Enkriptimi i Mesazhit: Duke përdorur çelësin sekret Bdash, serveri enkripton një mesazh të paracaktuar ("Hellofromtheserver") me enkriptimin Caesar dhe ia dërgon mesazhin e enkriptuar klientit.
•Mbyllja e Lidhjes: Serveri mbyll lidhjen pas dërgimit të mesazhit të enkriptuar.
Ky implementim demonstron një shkëmbim të thjeshtë të çelësave Diffie-Hellman dhe përdor një enkriptim të thjeshtë Caesar për mesazhin. Ai tregon procesin e vendosjes së një çelësi të përbashkët mbi një kanal të pasigurt, i cili më pas mund të përdoret për komunikim të sigurt.



