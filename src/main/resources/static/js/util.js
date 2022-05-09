

export const util = {
	getCookie : function(name) {
		var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	    
		return value? value[2] : null;
	}
}