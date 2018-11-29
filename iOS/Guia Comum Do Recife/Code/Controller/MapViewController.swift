//
//  MapViewController.swift
//  Guia Comum Do Recife
//
//  Created by Paula on 25/11/18.
//  Copyright © 2018 Paula Pithon. All rights reserved.
//

import UIKit
import FirebaseDatabase

class MapViewController: UIViewController {
    
    var ref: DatabaseReference!
    
    var mapaData = [Map]()
    var currentMapa = Map()
    
    var downloaded:Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        loadFirebase()
    }
    
    func loadFirebase() {
        ref = Database.database().reference()
        ref.child("mapa").observeSingleEvent(of: .value, with: { (snapshot) in
            
            self.downloaded = true
            for child in snapshot.children {
                let value = (child as! DataSnapshot).value as? NSDictionary
                
                let mapa = Map()
                mapa.nome = value?["nome"] as? String ?? ""
                
                var livros = [Livro]()
                let livrosMapa = value?["livros"] as? [NSDictionary]
                for livroMapa in livrosMapa! {
                    let livro = Livro()
                    livro.nome = livroMapa["nome"] as? String ?? ""
                    livro.resumo = livroMapa["resumo"] as? String ?? ""
                    livro.texto = livroMapa["texto"] as? String ?? ""
                    livro.local = livroMapa["local"] as? String ?? ""
                    livro.audio = livroMapa["audio"] as? String ?? ""
                    livro.imagens = (livroMapa["imagens"] as? [String]) ?? []
                    livros.append(livro)
                }
                mapa.livros = livros
                self.mapaData.append(mapa)
            
            }
        }) { (error) in
            print(error.localizedDescription)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let vc = segue.destination as? LivrosController {
            if let button = sender as? UIButton {
                print("prepare - button: \(button.accessibilityLabel ?? "")")
                for mapa in self.mapaData {
                    if mapa.nome == button.accessibilityLabel {
                        print("segue performing - \(mapa.nome)")
                        vc.mapa = mapa
                    }
                }
            }
        }
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String?, sender: Any?) -> Bool {
        if (!self.downloaded) {
        let errorAlert = UIAlertView(title: "Carregando conteúdo!", message: "Tente novamente em alguns segundinhos.", delegate: self, cancelButtonTitle: "OK")
        }
        return self.downloaded
    }

}
