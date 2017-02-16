printInvoice
============

This project will take the control of printers and will let to the user print sales diretily without asking for the printer settings.

To avoid issues with self signed certs we will need public/private key
here the steps to create private and public keys and import public key into trusted machines 

private

cmd>keytool -genkeypair -alias poloche -keyalg EC -keysize 571 -validity 730 -keystore mobius_keystore.jks
Enter keystore password:pagalibu81
What is your first and last name?
  [Unknown]:  Paolo Lizarazu
What is the name of your organizational unit?
  [Unknown]:  MB
What is the name of your organization?
  [Unknown]:  Mobius
What is the name of your City or Locality?
  [Unknown]:  Cochabamba-Bolivia
What is the name of your State or Province?
  [Unknown]:  Cochabamba
What is the two-letter country code for this unit?
  [Unknown]:  BO
Is CN=Paolo Lizarazu, OU=MB, O=Mobius, L=Cochabamba-Bolivia, ST=Cochabamba, C=BO correct?
Enter key password for <poloche> : pagalibu81


Public key
keytool -exportcert -keystore mobius_keystore.jks -alias poloche -file mobius_public.cer
Enter keystore password:pagalibu81
Certificate stored in file <mobius_public.cer>


import cert
for general users
keytool -importcert -keystore "c:\Program Files\Java\jdk1.8.0_77\jre\lib\security\cacerts" -alias poloche -file mobius_public.cer

for only current user
keytool -importcert -keystore "USER_HOME\AppData\LocalLow\Sun\Java\Deployment\security\trusted.certs" -alias poloche -file mobius_public.cer
