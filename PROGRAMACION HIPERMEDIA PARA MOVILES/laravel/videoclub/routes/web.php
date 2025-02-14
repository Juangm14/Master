<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CatalogController;
use App\Http\Controllers\HomeController;


Auth::routes();

Route::get('/', [HomeController::class, 'index']);
    Route::middleware(['auth'])->group(function () {

    Route::get('/catalog', [CatalogController::class, 'getIndex']);

    Route::get('/catalog/show/{id}', [CatalogController::class, 'getShow']) -> name('catalog.show');

    Route::get('/catalog/edit/{id}', [CatalogController::class, 'getEdit']);

    Route::get('/catalog/create', function () {return view('catalog.create');});
    
    Route::post('/catalog/create', [CatalogController::class, 'postCreate']);

    Route::put('/catalog/edit/{id}', [CatalogController::class, 'putEdit']);

    Route::put('/catalog/rent/{id}', [CatalogController::class, 'putRent']);
    Route::put('/catalog/return/{id}', [CatalogController::class, 'putReturn']);
    Route::delete('/catalog/delete/{id}', [CatalogController::class, 'deleteMovie']);

});
