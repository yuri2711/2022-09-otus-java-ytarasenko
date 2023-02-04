function createClient() {

    fetch('http://localhost:8080/api/client', {
        method: 'POST',
        body: JSON.stringify({
            name: window.newClientForm.newName.value,
            login: window.newClientForm.newLogin.value,
            password: window.newClientForm.newPassword.value,
            address: { street: window.newClientForm.newAddress.value },
            phones: [ { number: window.newClientForm.newPhone.value} ]
        }),
        headers: {"Content-type": "application/json; charset=UTF-8"}
    })
        .then(response => {
            window.newClientForm.newName.value = "";
            window.newClientForm.newLogin.value = "";
            window.newClientForm.newPassword.value = "";
            window.newClientForm.newAddress.value = "";
            window.newClientForm.newPhone.value = "";
        });
}