

$(document).ready(function() {
		
	var chatPane = document.getElementById("chat");	
	var id = 0;
	side="other";	
	author = "Bot";
	
	if(!$('#chat ul').last().hasClass(side)){
		$('#chat').append('<ul class="'+side+'"></ul>');
	}
	$('#chat ul.'+side).last().append('<li><p class="message-wrapper m-a-0 '+side+'" data-author="'+"Bot: "+'"><span class="message">'+"Hi I'm botty, nice to meet you. I'm here to help you discover what your issues are, so I'll ask you several questions and depending on you answers I'll make my diagnosis. There'll be some yes/no questions for which you'll have to answer with 'Yes' or 'No' for me to understand you better. So first what brings you here?"+'</span></p></li>');  

	
	/**
	 * méthode pour le rendu des messages
	 */ 
	var render = function(side,author,msg,resp){
		
		author="User";
		side="self";
		// ajout du message dans la fenetre					
		$('#chat ul.'+"other").last().append('<li><p class="message-wrapper m-a-0 '+side+'" data-author="'+author+'"><span class="message">'+msg+'</span></p></li>');
	    
		side="other";
		//ajout du message du bot
		setTimeout( ($('#chat ul.'+"other").last().append('<li><p class="message-wrapper m-a-0 '+side+'" data-author="'+"Bot: "+'"><span class="message">'+resp+'</span></p></li>')) , 10000);
		
		// scroller jusqu'au bas de la fenetre
	    chatPane.scrollTop = chatPane.scrollHeight;
	}
	
	/**
	 * méthode pour l'envoie des messages
	 */
	var send = function(msg){
		var newMsg =".";
		if(msg.length>0){
			$.post('chat',{msg: msg,author: $('textarea').attr('data-author')}, function(data) {
			    newMsg = data.split('+');
			    id= newMsg[2];
			    response= newMsg[3];
			    render("self",newMsg[0],newMsg[1], newMsg[3]);
			    $('textarea').val('').focus();
		    });
		}
		
	}
	
	/* Evenements */
	$('button#submit').click(function(e){
		send($('textarea').val());
	});
	$(document).keyup(function(e) {
		e.preventDefault();
	    if(e.which == 13 && !e.shiftKey) {
	    	var msg = $('textarea').val();
	    	if(msg.trim().length>0){
		        send(msg);	    
		        refresh();
	    	}else{
	    		$('textarea').val("");
	    	}
	    }
	});
	
//-- end of file --
	
});
