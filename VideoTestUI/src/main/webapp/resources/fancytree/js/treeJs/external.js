var external = {
	access : "",
	save : function(access, startDate, endDate, memo, fileId) {
		$.ajax({
			url : "externalSave",
			data : {
				access : external.access,
				startDate : startDate,
				endDate : endDate,
				memo : memo,
				fileId : fileId
			},
			success : function(extern) {
				external.modalResult(extern);
			},
			error : function() {
				
			}
		});
	},
	modalResult : function(extern) {
		var defaultURI = '112.160.65.103:8889/video';
		
		if ( extern.access == 'private') {
			$('#uriCreate').html(defaultURI + '/guest/' + extern.uri);
			$('#securityCode').html(extern.external);
			
			$('.defaultResults').show();
			$('.securityResult').show();
		}
		else if ( extern.access == 'public') {					
			$('#uriCreate').html(defaultURI + '/guest/' + extern.uri);
			
			$('.defaultResults').show();
		}
	},
	getStartDate : function() {
		var date = new Date();
					
		return date;
	},
	getDateString : function(date) {
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		var hour = date.getHours();
		var minute = date.getMinutes();
					
		if ( ("" + month).length == 1)
			month = '0' + month;
		if ( ("" + day).length == 1)
			day = '0' + day;
		if ( ("" + minute).length == 1)
			minute = '0' + minute;
		
		var result = year + '-' + month + '-' + day + ' ' + hour + ':' + minute;
		
		return result;
	}
}