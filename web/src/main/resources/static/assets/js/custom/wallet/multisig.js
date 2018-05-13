(function () {

	const address = $('#currentAddress').data('hash');
	const multisigAddress = $('#multisigAddress').data('hash');

	const multisigContract = CindercloudWeb3.getGlobalWeb3().eth.contract(abi.multisig).at(multisigAddress);


	let multisigData = {
		requiredOwners: 0,
		owners:  []
	};

	new Vue({
		el: '#multisigApp',
		data: multisigData,
		created: function () {
			multisigContract.m_required.call(function (err, result) {
				multisigData.requiredOwners = result.toNumber();
			});
			multisigContract.
		}
	});
})();