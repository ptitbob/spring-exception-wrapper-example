> Initialisation - to be completed

Accès ping
(*erreur gérée au niveau de la classe d'exception*)
:

```bash
http GET :8080/ping
```

Acces un utilisateur provoquant une erreur d'utilisateur non trouvé
(*Gestion spécifique de l'erreur*)
:

```bash
http GET :8080/users/2
```

Création utilisateur provoquant une erreur de validation de formulaire : 

```bash
http -f POST :8080/users 
```

Création d'utilisateur provoquant une erreur de création
(*Gestion spécifique de l'erreur*)
: 

```bash
http -f POST :8080/users login="toto" firstname="toto & titi"
```