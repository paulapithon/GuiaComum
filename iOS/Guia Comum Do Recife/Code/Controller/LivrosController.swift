//
//  LivrosController.swift
//  Guia Comum Do Recife
//
//  Created by Paula on 25/11/18.
//  Copyright © 2018 Paula Pithon. All rights reserved.
//

import UIKit

class LivrosController: UIViewController {
    
    var mapa = Map()
    
    @IBOutlet weak var image: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        print("mapa name: \(mapa.nome)")
        image.image = UIImage(named: mapa.nome)
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let vc = segue.destination as? LivroController {
            vc.mapa = self.mapa
        }
    }
 

}
