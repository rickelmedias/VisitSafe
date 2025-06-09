import { useState } from 'react'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Textarea } from '@/components/ui/textarea'
import { useJustifyServiceProviderRelease } from '@/api/hooks/useJustifyServiceProviderRelease'
import { useCheckoutRelease } from '@/api/hooks/useCheckoutRelease'
import { Label } from '@/components/ui/label'
import { Upload } from 'lucide-react'
import { useToast } from '@/hooks/use-toast'

interface Props {
  release: any
  onSuccess: () => void
  onCancel: () => void
}

export default function LateExitForm({ release, onSuccess, onCancel }: Props) {
  const [justification, setJustification] = useState('')
  const [file, setFile] = useState<File | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const { toast } = useToast()

  const justifyMutation = useJustifyServiceProviderRelease({
    mutation: {
      onSuccess: () => {
        // After justification is submitted, proceed with checkout
        checkoutMutation.mutate({ id: release.id })
      },
      onError: () => {
        setIsSubmitting(false)
        toast({
          title: "Erro ao enviar justificativa",
          description: "Não foi possível enviar a justificativa. Tente novamente.",
          variant: "destructive",
        })
      }
    }
  })

  const checkoutMutation = useCheckoutRelease({
    mutation: {
      onSuccess: () => {
        setIsSubmitting(false)
        toast({
          title: "Saída registrada",
          description: "A saída foi registrada com sucesso.",
        })
        onSuccess()
      },
      onError: () => {
        setIsSubmitting(false)
        toast({
          title: "Erro ao registrar saída",
          description: "Não foi possível registrar a saída. Tente novamente.",
          variant: "destructive",
        })
      }
    }
  })

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0])
    }
  }

  const handleSubmit = async () => {
    if (!justification || !file) {
      toast({
        title: "Campos obrigatórios",
        description: "Por favor, preencha a justificativa e anexe um documento.",
        variant: "destructive",
      })
      return
    }

    setIsSubmitting(true)
    justifyMutation.mutate({
      id: release.id,
      data: justification
    })
  }

  return (
    <div className="space-y-4">
      <div>
        <h3 className="text-lg font-semibold">Justificativa de Saída Tardia</h3>
        <p className="text-sm text-gray-500">
          Por favor, forneça uma justificativa detalhada e um documento assinado explicando o motivo da saída após o horário permitido.
          A saída só será registrada após o envio da justificativa.
        </p>
      </div>

      <div className="space-y-2">
        <Label>Justificativa</Label>
        <Textarea
          placeholder="Descreva o motivo da saída tardia..."
          value={justification}
          onChange={(e) => setJustification(e.target.value)}
          className="min-h-[100px]"
        />
      </div>

      <div className="space-y-2">
        <Label>Documento Assinado</Label>
        <div className="flex items-center gap-2">
          <Input
            type="file"
            accept="image/*,.pdf"
            onChange={handleFileChange}
            className="flex-1"
          />
          {file && (
            <span className="text-sm text-gray-500">
              {file.name}
            </span>
          )}
        </div>
      </div>

      <div className="flex gap-2">
        <Button
          variant="outline"
          onClick={onCancel}
          disabled={isSubmitting}
          className="flex-1"
        >
          Cancelar
        </Button>
        <Button
          onClick={handleSubmit}
          disabled={!justification || !file || isSubmitting}
          className="flex-1"
        >
          {isSubmitting ? 'Enviando...' : 'Enviar Justificativa'}
        </Button>
      </div>
    </div>
  )
} 