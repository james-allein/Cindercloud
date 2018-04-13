(function () {
	$('#functionSelect').change(function (_) {

		var data = {
			elementName: $(this).val(),
			elements: $('#currentAbi').data('abi')
		};

		$.ajax({
			type: 'POST',
			url: '/contract/generate-ui',
			data: JSON.stringify(data), // or JSON.stringify ({name: 'jonas'}),
			success: function (data) {
				$('#generatedInput').html(data)
			},
			contentType: "application/json",
			dataType: 'html',
			accepts: 'html'
		});
	});
})();