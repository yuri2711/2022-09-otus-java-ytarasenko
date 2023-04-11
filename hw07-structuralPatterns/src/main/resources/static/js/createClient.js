function createClient() {

    fetch('http://localhost:8080/api/client', {
        method: 'POST',
        body: JSON.stringify({
            name: window.newClientForm.name.value,
            address: { street: window.newClientForm.address.value },
            phones: [
                { number: window.newClientForm.phone_mob.value },
                { number: window.newClientForm.phone_home.value }
            ]
        }),
        headers: {"Content-type": "application/json; charset=UTF-8"}
    })
        .then(response => {
            getAllClients();
        });
}