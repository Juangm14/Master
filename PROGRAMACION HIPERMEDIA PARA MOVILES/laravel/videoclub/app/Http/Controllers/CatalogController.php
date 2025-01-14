<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Movie;

class CatalogController extends Controller
{

    public function getIndex()
    {
		$peliculas = Movie::all();
		return view('catalog.index', ['peliculas' => $peliculas]);
    }

    public function getShow($id)
    {
		$pelicula = Movie::findOrFail($id); 

        if ($pelicula) {
            return view('catalog.show', ['pelicula' => $pelicula, 'id' => $id]);
        }

        abort(404, 'Película no encontrada');
    }

    public function getCreate()
    {
        return view('catalog.create');
    }

	public function getEdit($id)
	{
		$pelicula = Movie::findOrFail($id);
	
		if ($pelicula) {
			return view('catalog.edit', ['pelicula' => $pelicula, 'id' => $id]);
		}
	
		abort(404, 'Película no encontrada');
	}
	
}
