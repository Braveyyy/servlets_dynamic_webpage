function searchUsers() {
    var searchInput = document.getElementById('searchInput').value.trim();
    if (searchInput === "") {
        alert("Please enter a user name to search.");
        return;
    }

    fetch('/user_management_system/search?userName=' + searchInput)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displaySearchResult(data);
        })
        .catch(error => console.error('Error:', error));
}

function displaySearchResult(users) {
    var searchResultDiv = document.getElementById('searchResult');
    searchResultDiv.innerHTML = "";

    if (users.length === 0) {
        searchResultDiv.textContent = "No users found.";
    } else {
        var userList = document.createElement('ul');
        users.forEach(user => {
            var listItem = document.createElement('li');
            listItem.textContent = user.userName + ' - ' + user.userType; // Update property names according to the backend
            userList.appendChild(listItem);
        });
        searchResultDiv.appendChild(userList);
    }
}

function addUser() {
    var newUserID = document.getElementById('newUserID').value.trim();
    var newUserName = document.getElementById('newUserName').value.trim();
    var newUserType = document.getElementById('newUserType').value.trim();
    if(newUserID === "" || newUserName === "" || newUserType === "") {
        alert("Missing a component, please enter a UserID, UserName, and UserType.");
        return;
    }

    fetch('/user_management_system/addUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            UserID: newUserID,
            UserName: newUserName,
            UserType: newUserType
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displayAddResult(data);
        })
        .catch(error => console.error('Error:', error));
}

function displayAddResult(message) {
    var addUserResultDiv = document.getElementById('addUserResult');
    addUserResultDiv.innerHTML = "";

    if(message.length === 0) {
        addUserResultDiv.textContent = "Unable to add user.";
    }
    else{
        addUserResultDiv.textContent = message;
    }
}

function updateUser() {
    var oldUserID = document.getElementById('oldUserID').value.trim();;
    var updateUserName = document.getElementById('updateUserName').value.trim();;
    var updateUserType = document.getElementById('updateUserType').value.trim();;
    if(oldUserID === "" || updateUserName === "" || updateUserType === "") {
        alert("Missing a component, please enter the old ID, new username, and new user type.");
        return;
    }

    fetch('/user_management_system/updateUser', {
        method: 'POST',
        body: oldUserID, updateUserName, updateUserType
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displayUpdateResult(data)
        })
        .catch(error => console.error('Error:', error));
}

function displayUpdateResult(message) {
    var updateUserResultDiv = document.getElementById('updateUserResult');
    updateUserResultDiv.innerHTML = "";

    if(message.length === 0) {
        updateUserResultDiv.textContent = "Unable to update user.";
    }
    else{
        updateUserResultDiv.textContent = message;
    }
}

function deleteUser() {
    var deleteUserID = document.getElementById('deleteUserID').value.trim();;
    if(deleteUserID === "") {
        alert("Please enter a user to be deleted.");
        return 0;
    }

    fetch('/user_management_system/deleteUser', {
        method: 'POST',
        body: deleteUserID
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displayDeleteResult(data)
        })
        .catch(error => console.error('Error:', error));
}

function displayDeleteResult(message) {
    var deleteUserResultDiv = document.getElementById('deleteUserResult');
    deleteUserResultDiv.innerHTML = "";

    if(message.length === 0) {
        deleteUserResultDiv.textContent = "Unable to delete user.";
    }
    else{
        deleteUserResultDiv.textContent = message;
    }
}
