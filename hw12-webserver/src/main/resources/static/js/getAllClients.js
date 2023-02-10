function getAllClients(){
    let tHead = document.getElementById("tHead");
    tHead.innerHTML = '<tr> <th>ID</th> <th>Name</th> <th>Login</th> <th>Address</th> <th>Phones</th> </tr>';


    let tBody = document.getElementById("tBody");
    tBody.innerHTML = "";

    fetch('http://localhost:8080/api/clients')
        .then(response => response.json())
        .then(clients => {
            clients.forEach(function (client) {

                //заполняем таблицу информацией юзера
                var row = tBody.insertRow();
                var cell0 = row.insertCell();
                cell0.innerHTML = client.id;
                var cell1 = row.insertCell();
                cell1.innerHTML = client.name;
                var cell2 = row.insertCell();
                cell2.innerHTML = client.login;
                var cell3 = row.insertCell();
                cell3.innerHTML = client.address.street;

                var phoneList = document.createElement('ul');
                client.phones.forEach(function (phone) {
                    let li = document.createElement('li');
                    li.textContent = phone.number + " ";
                    phoneList.appendChild(li);
                });
                var cell4 = row.insertCell();
                cell4.innerHTML = phoneList.textContent;
            })
        });
}
