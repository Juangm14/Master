<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Movie;
use Alert;

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

    public function postCreate(Request $request)
    {
        $pelicula = new Movie();
        $pelicula->title = $request->title;
        $pelicula->year = $request->year;
        $pelicula->director = $request->director;
        $pelicula->poster = $request->poster;
        $pelicula->synopsis = $request->synopsis;
        $pelicula->rented = false;
        $pelicula->save();

        Alert::success('La película ha sido creada correctamente.');

        return redirect('/catalog');
    }

    public function getEdit($id)
    {
        $pelicula = Movie::findOrFail($id);
    
        if ($pelicula) {
            return view('catalog.edit', ['pelicula' => $pelicula, 'id' => $id]);
        }
    
        abort(404, 'Película no encontrada');
    }

    public function putEdit(Request $request, $id)
    {
        $pelicula = Movie::findOrFail($id);
        $pelicula->title = $request->title;
        $pelicula->year = $request->year;
        $pelicula->director = $request->director;
        $pelicula->poster = $request->poster;
        $pelicula->synopsis = $request->synopsis;
        $pelicula->save();

        Alert::success('La película ha sido editada correctamente.');

        return redirect()->route('catalog.show', ['id' => $pelicula->id]);
    }

    public function putRent($id)
    {
        $pelicula = Movie::findOrFail($id);
        $pelicula->rented = true;
        $pelicula->save();

        Alert::success('La película ha sido alquilada correctamente.');

        return redirect()->route('catalog.show', $id);
    }

    public function putReturn($id)
    {
        $pelicula = Movie::findOrFail($id);
        $pelicula->rented = false;
        $pelicula->save();

        Alert::success('La película ha sido devuelta correctamente.');

        return redirect()->route('catalog.show', $id);
    }

    public function deleteMovie($id)
    {
        $pelicula = Movie::findOrFail($id);
        $pelicula->delete();
    
        Alert::success('La película ha sido eliminada correctamente.');
    
        return redirect()->route('catalog.index');
    }
    
}   
