<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CatalogController;
use App\Http\Controllers\HomeController;


Auth::routes();

//Routes públicas
Route::get('/', [HomeController::class, 'index']);

Route::get('/catalog', [CatalogController::class, 'getIndex']);

Route::get('/catalog/show/{id}', [CatalogController::class, 'getShow']);

Route::get('/catalog/edit/{id}', [CatalogController::class, 'getEdit']);

Route::get('/catalog/create', function () {
    return view('catalog.create');
});
