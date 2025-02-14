<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\APICatalogController;
use App\Http\Middleware\AuthenticateOnceWithBasicAuth;

Route::get('v1/catalog', [APICatalogController::class, 'index']);
Route::get('v1/catalog/{id}', [APICatalogController::class, 'show']);

Route::middleware([AuthenticateOnceWithBasicAuth::class])->group(function () {
    Route::post('v1/catalog', [APICatalogController::class, 'store']);
    Route::put('v1/catalog/{id}', [APICatalogController::class, 'update']);
    Route::delete('v1/catalog/{id}', [APICatalogController::class, 'destroy']);
    Route::put('v1/catalog/{id}/rent', [APICatalogController::class, 'putRent']);
    Route::put('v1/catalog/{id}/return', [APICatalogController::class, 'putReturn']);
});
