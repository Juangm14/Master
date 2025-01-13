<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CatalogController;
use App\Http\Controllers\HomeController;

Route::get('/', function () {
    return view('home');
});

Route::get('/login', function () {
    return view('auth.login');
});

Route::get('/catalog', [CatalogController::class, 'getIndex']);

Route::get('/catalog/show/{id}', [CatalogController::class, 'getShow']);

Route::get('/catalog/edit/{id}', [CatalogController::class, 'getEdit']);

Route::get('/catalog/create', function () {
    return view('catalog.create');
});



Route::get('/logout', function () {
    return "Logout usuario";
});
