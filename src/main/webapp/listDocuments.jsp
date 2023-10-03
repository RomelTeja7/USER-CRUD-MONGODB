<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <title>List Users</title>

    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand mb-0 h1" href="index.jsp">USER CRUD</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item active">
                        <a class="nav-link" href="index.jsp">Home <span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="listDocuments?action=list">User List</a>
                    </li>
                </ul>
            </div>
        </nav>
        <c:if test="${not empty param.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert" id="successAlert">
                ${param.successMessage}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>

        <c:if test="${not empty param.errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert" id="errorAlert">
                ${param.errorMessage}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        <div class="container mt-5">
            <h1 class="mb-5">USER LIST</h1>
            <button type="button" class="btn btn-success  mb-3" data-toggle="modal" data-target="#addModalCenter">Add</button>
            <table class="table table-bordered table-dark table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">First Name</th>
                        <th scope="col">Last Name</th>
                        <th scope="col">Email</th>
                        <th scope="col">Age</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${documents}" var="document">
                        <tr>
                            <td>${document._id}</td>
                            <td>${document.first_name}</td>
                            <td>${document.last_name}</td>
                            <td>${document.email}</td>
                            <td>${document.age}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-toggle="modal" data-target="#seeModalCenter${document._id}">See</button>
                                <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#editModalCenter${document._id}">Edit</button>
                                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteModalCenter${document._id}">Delete</button>
                            </td>
                        </tr>
                        <!-- ADD MODAL -->
                    <div class="modal fade" id="addModalCenter" tabindex="-1" role="dialog" aria-labelledby="addModalCenterLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="addModalLabel">ADD</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <form action="listDocuments?action=add" method="post">
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label class="col-form-label">First name</label>
                                            <input type="text" class="form-control" name="first_name" required>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Last name</label>
                                            <input type="text" class="form-control"  name="last_name" required> 
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Email</label>
                                            <input type="email" class="form-control"  name="email" required> 
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Age</label>
                                            <input type="number" class="form-control"  name="age" required> 
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                        <button type="submit"  class="btn btn-success">Add</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <!-- SEE MODAL -->
                    <div class="modal fade" id="seeModalCenter${document._id}" tabindex="-1" role="dialog" aria-labelledby="seeModalCenterLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="seeModalLabel">SEE</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <div class="form-group">
                                        <label class="col-form-label">First name</label>
                                        <input type="text" class="form-control"  value="${document.first_name}" readonly>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-form-label">Last name</label>
                                        <input type="text" class="form-control"  value="${document.last_name}" readonly> 
                                    </div>
                                    <div class="form-group">
                                        <label class="col-form-label">Email</label>
                                        <input type="email" class="form-control" value="${document.email}" readonly> 
                                    </div>
                                    <div class="form-group">
                                        <label class="col-form-label">Age</label>
                                        <input type="text" class="form-control" value="${document.age}" readonly> 
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- EDIT MODAL -->
                    <div class="modal fade" id="editModalCenter${document._id}" tabindex="-1" role="dialog" aria-labelledby="editModalCenterLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="editModalLabel">EDIT</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">                           
                                    <form action="listDocuments?action=edit" method="post">
                                        <div class="form-group">
                                            <label class="col-form-label">ID</label>
                                            <input type="text" class="form-control" name="userId" value="${document._id}" readonly>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">First name</label>
                                            <input type="text" class="form-control"  name="first_name" value="${document.first_name}"  required>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Last name</label>
                                            <input type="text" class="form-control"  name="last_name" value="${document.last_name}" required> 
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Email</label>
                                            <input type="text" class="form-control"  name="email" value="${document.email}" required> 
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Age</label>
                                            <input type="number" class="form-control"  name="age" value="${document.age}" required> 
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                            <button type="submit" class="btn btn-warning">Edit</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- DELETE MODAL -->
                    <div class="modal fade" id="deleteModalCenter${document._id}" tabindex="-1" role="dialog" aria-labelledby="deleteModalCenterTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="deleteModalLongTitle">DELETE</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    Do you want to delete the user ${document.first_name} ${document.last_name}?
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <form action="listDocuments?action=delete" method="post">
                                        <input type="hidden" name="userId" value="${document._id}">
                                        <button type="submit" class="btn btn-danger">DELETE</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <script>
                    //Close alert
                    setTimeout(function () {
                        $("#successAlert").alert('close');
                    }, 4000);
                    setTimeout(function () {
                        $("#errorAlert").alert('close');
                    }, 4000);
                </script>
                <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
                </body>
                </html>
