'use strict';

require(["moment", 'angular', 'leaflet'], function(moment, angular, L) {

    angular.merge(L.drawLocal, {
        "draw": {
            "toolbar": {
                "actions": {
                    "title": "Annulla il disegno",
                    "text": "Annulla"
                },
                "finish": {
                    "title": "Termina il disegno",
                    "text": "Termina"
                },
                "undo": {
                    "title": "Elimina l'ultimo punto disegnato",
                    "text": "Elimina l'ultimo punto"
                },
                "buttons": {
                    "polyline": "Draw a polyline",
                    "polygon": "Disegna un poligono",
                    "rectangle": "Disegna un rettangolo",
                    "circle": "Draw a circle",
                    "marker": "Draw a marker"
                }
            },
            "handlers": {
                "circle": {
                    "tooltip": {
                        "start": "Click and drag to draw circle."
                    },
                    "radius": "Radius"
                },
                "marker": {
                    "tooltip": {
                        "start": "Click map to place marker."
                    }
                },
                "polygon": {
                    "tooltip": {
                        "start": "Click per iniziare a disegnare.",
                        "cont": "Click per continuare a disegnare.",
                        "end": "Click sul primo punto per chiudere il poligono."
                    }
                },
                "polyline": {
                    "error": "<strong>Error:</strong> shape edges cannot cross!",
                    "tooltip": {
                        "start": "Click to start drawing line.",
                        "cont": "Click to continue drawing line.",
                        "end": "Click last point to finish line."
                    }
                },
                "rectangle": {
                    "tooltip": {
                        "start": "Click e trascina per disegnare un rettangolo."
                    }
                },
                "simpleshape": {
                    "tooltip": {
                        "end": "Release mouse to finish drawing."
                    }
                }
            }
        },
        "edit": {
            "toolbar": {
                "actions": {
                    "save": {
                        "title": "Save changes.",
                        "text": "Save"
                    },
                    "cancel": {
                        "title": "Cancel editing, discards all changes.",
                        "text": "Cancel"
                    }
                },
                "buttons": {
                    "edit": "Edit layers.",
                    "editDisabled": "No layers to edit.",
                    "remove": "Delete layers.",
                    "removeDisabled": "No layers to delete."
                }
            },
            "handlers": {
                "edit": {
                    "tooltip": {
                        "text": "Drag handles, or marker to edit feature.",
                        "subtext": "Click cancel to undo changes."
                    }
                },
                "remove": {
                    "tooltip": {
                        "text": "Click on a feature to remove"
                    }
                }
            }
        }
    });
});
