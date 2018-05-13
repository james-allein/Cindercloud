(function () {

	const address = $('#currentAddress').data('hash');
	const multisigAddress = $('#multisigAddress').data('hash');

	let multisigData = {

	};

	new Vue({
		el: '#multisigApp',
		data: multisigData
	});
})();