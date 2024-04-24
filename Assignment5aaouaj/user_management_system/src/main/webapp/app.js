// SEARCH FOR USERS
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

// ADD NEW USER
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
            userId: newUserID,
            userName: newUserName,
            userType: newUserType
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

// UPDATE USER
function updateUser() {
    var oldUserID = document.getElementById('oldUserID').value.trim();
    var updateUserName = document.getElementById('updateUserName').value.trim();
    var updateUserType = document.getElementById('updateUserType').value.trim();
    if(oldUserID === "" || updateUserName === "" || updateUserType === "") {
        alert("Missing a component, please enter the old ID, new username, and new user type.");
        return;
    }

    fetch('/user_management_system/updateUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            userId: oldUserID,
            userName: updateUserName,
            userType: updateUserType
        })
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

// DELETE USER
function deleteUser() {
    var deleteUserID = document.getElementById('deleteUserID').value.trim();
    if(deleteUserID === "") {
        alert("Please enter a user to be deleted.");
        return 0;
    }

    fetch('/user_management_system/deleteUser?userId=' + deleteUserID, {
        method: 'DELETE'
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

// ADD USAGE RECORD
function addUsage() {
    var usageUserID = document.getElementById('usageUserID').value.trim();
    var usageDeviceID = document.getElementById('usageDeviceID').value.trim();
    var uDate = document.getElementById('uDate').value.trim();
    var uDuration = document.getElementById('uDuration').value.trim();

    if(usageUserID === "" || usageDeviceID === "" || uDate === "" || uDuration === "") {
        alert("Missing a component, please enter the UserID, DeviceID, Date of Usage, and Usage Duration.");
        return;
    }

    fetch('/user_management_system/addUsage', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            userId: usageUserID,
            deviceId: usageDeviceID,
            usageDate: uDate,
            usageDuration: uDuration
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        displayAddUsageResult(data)
    })
    .catch(error => console.error('Error:', error));
}

function displayAddUsageResult(message) {
    var addUsageResultDiv = document.getElementById('addUsageResult');
    addUsageResultDiv.innerHTML = "";

    if(message.length === 0) {
        addUsageResultDiv.textContent = "Unable to add usage.";
    }
    else{
        addUsageResultDiv.textContent = message;
    }
}

// SEARCH FOR USER USAGE WITHIN DATE RANGE
function searchUsage() {
    var searchUserID = document.getElementById('searchUserID').value.trim();
    var start_date = document.getElementById('start_date').value.trim();
    var end_date = document.getElementById('end_date').value.trim();

    if(searchUserID === "" || start_date === "" || end_date === "") {
        alert("Missing a component, please enter a UserID, Start Date, and End Date.");
        return;
    }

    fetch('/user_management_system/searchUsage?userId=' + searchUserID + '&startDate=' + start_date + '&endDate=' + end_date)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displaySearchUsageResult(data);
        })
        .catch(error => console.error('Error:', error));
}

function displaySearchUsageResult(message) {
    var searchUsageResultDiv = document.getElementById('searchUsageResult');
    searchUsageResultDiv.innerHTML = "";

    if (message.length === 0) {
        searchUsageResultDiv.textContent = "No usage found.";
    } else {
        var usageList = document.createElement('ul');
        message.forEach(uses => {
            var listItem = document.createElement('li');
            listItem.textContent = uses.userName + ': ' + uses.deviceName + ' - ' + uses.usageDate + ' - ' + uses.usageDuration; // Update property names according to the backend
            usageList.appendChild(listItem);
        });
        searchUsageResultDiv.appendChild(usageList);
    }
}
