//
//  DescriptionController.swift
//  Guia Comum Do Recife
//
//  Created by Paula on 28/11/18.
//  Copyright © 2018 Paula Pithon. All rights reserved.
//

import UIKit

class DescriptionController: UIViewController {
    
    var mapa = Map()
    var count:Int = 0

    @IBOutlet weak var labelText: UILabel!
    @IBOutlet weak var descriptionText: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        descriptionText.text = mapa.livros[count].texto
        labelText.text = mapa.livros[count].nome
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let vc = segue.destination as? LivrosController {
            vc.mapa = mapa
        }
    }

}
