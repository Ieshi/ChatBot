
<!DOCTYPE html>
<html lang="fr">
<head>
	<title>Chat</title>
	<meta charset="UTF-8">
	
	<link href="./css/bootstrap.min.css" rel="stylesheet">
	<link href="./css/styles.css" rel="stylesheet">
</head>
<body class="grey lighten-5">
	

	<div class="container text-center">
		
		<hr>		
		
		<div class="row">
			<div class="text-center col-md-6 col-md-offset-3 chat-panel">
				
				<div id="chat" class="chat" >
					
				</div>
				<textarea type="text"  id="message" class="form-control" name="message" placeholder="..."></textarea>
				<br>
				<button class="btn btn-primary btn-block btn-lg" id="submit" name="submit">Envoyer</button>
			</div>
		</div>
		
	</div>

	
	<script src="./js/jquery.min.js"></script>
	<script src="./js/app.js"></script>
</body>
</html>